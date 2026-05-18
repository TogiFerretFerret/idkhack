package sh.idktheflag.idk.api.management.network;

import sh.idktheflag.idk.api.utils.discord.DiscordEventHandlers;
import sh.idktheflag.idk.api.utils.discord.DiscordRPC;
import sh.idktheflag.idk.api.utils.discord.DiscordRichPresence;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.IdkHackMod;
import sh.idktheflag.idk.impl.features.modules.client.RPC;

import java.io.*;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Discord Rich Presence.
 * On Unix: direct IPC socket (no native lib needed), same protocol as presencectl.nvim.
 * On Windows: falls back to the native discord-rpc JNA library.
 */
public class DiscordPresence implements IMinecraft {

    private static final boolean IS_WINDOWS = System.getProperty("os.name", "").toLowerCase().contains("win");

    private static final String CLIENT_ID = "1273333927096094742";
    private static final int OP_HANDSHAKE = 0;
    private static final int OP_FRAME     = 1;

    private static SocketChannel socket;
    private static Thread thread;
    private static long startTimestamp;

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public static void start() {
        if (IS_WINDOWS) { startWindows(); return; }
        if (thread != null) thread.interrupt();
        startTimestamp = System.currentTimeMillis() / 1000L;
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (socket == null || !socket.isConnected()) connect();
                    sendActivity();
                    Thread.sleep(15_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception e) {
                    System.err.println("[idk] Discord IPC error: " + e.getMessage());
                    closeSocket();
                    try { Thread.sleep(5_000); } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }, "Discord-IPC-Thread");
        thread.setDaemon(true);
        thread.start();
    }

    public static synchronized void stop() {
        if (IS_WINDOWS) { stopWindows(); return; }
        if (thread != null) { thread.interrupt(); thread = null; }
        closeSocket();
    }

    // -------------------------------------------------------------------------
    // Windows fallback — native discord-rpc via JNA
    // -------------------------------------------------------------------------

    private static DiscordRichPresence winPresence;
    // Lazy: only resolve the native lib on Windows so Linux never tries to load discord-rpc.so
    private static DiscordRPC winRpc;
    private static DiscordRPC getWinRpc() {
        if (winRpc == null) winRpc = DiscordRPC.INSTANCE;
        return winRpc;
    }

    private static void startWindows() {
        DiscordRPC rpc = getWinRpc();
        if (rpc == null) {
            System.out.println("[idk] Discord RPC native library not available, skipping");
            return;
        }
        if (thread != null) thread.interrupt();
        winPresence = new DiscordRichPresence();
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize(CLIENT_ID, handlers, true, "");
        startTimestamp = System.currentTimeMillis() / 1000L;
        winPresence.startTimestamp = startTimestamp;
        winPresence.details = RPC.INSTANCE != null ? RPC.INSTANCE.text.getValue() : "";
        winPresence.state = getState();
        winPresence.largeImageKey = getImageKey();
        winPresence.largeImageText = "catogod.cc";
        winPresence.smallImageKey = "cop26logo_cropped_1_";
        winPresence.smallImageText = "Version: " + IdkHackMod.VERSION;
        rpc.Discord_UpdatePresence(winPresence);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try { Thread.sleep(2000); } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                rpc.Discord_RunCallbacks();
                winPresence.state = getState();
                winPresence.largeImageKey = getImageKey();
                rpc.Discord_UpdatePresence(winPresence);
            }
        }, "Discord-RPC-Callback-Handler");
        thread.setDaemon(true);
        thread.start();
    }

    private static void stopWindows() {
        DiscordRPC rpc = getWinRpc();
        if (rpc == null) return;
        if (thread != null) { thread.interrupt(); thread = null; }
        rpc.Discord_Shutdown();
    }

    // -------------------------------------------------------------------------
    // Socket management
    // -------------------------------------------------------------------------

    private static void connect() throws IOException {
        Path socketPath = findIpcSocket();
        if (socketPath == null) throw new IOException("No Discord IPC socket found");

        socket = SocketChannel.open(StandardProtocolFamily.UNIX);
        socket.connect(UnixDomainSocketAddress.of(socketPath));

        // Handshake
        String handshake = "{\"v\":1,\"client_id\":\"" + CLIENT_ID + "\"}";
        writeFrame(OP_HANDSHAKE, handshake);
        readFrame(); // consume READY response
    }

    private static void closeSocket() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
        socket = null;
    }

    // -------------------------------------------------------------------------
    // Activity
    // -------------------------------------------------------------------------

    private static void sendActivity() throws IOException {
        String nonce = UUID.randomUUID().toString();
        long pid = ProcessHandle.current().pid();
        String activity = buildActivity();

        String payload = "{"
                + "\"cmd\":\"SET_ACTIVITY\","
                + "\"nonce\":\"" + nonce + "\","
                + "\"args\":{"
                + "\"activity\":" + activity + ","
                + "\"pid\":" + pid
                + "}"
                + "}";

        writeFrame(OP_FRAME, payload);
        readFrame();
    }

    private static String buildActivity() {
        String details = RPC.INSTANCE != null ? jsonEscape(RPC.INSTANCE.text.getValue()) : "catogod.cc";
        String state   = getState();
        String largeKey = getImageKey();

        return "{"
                + "\"details\":\"" + details + "\","
                + "\"state\":\"" + state + "\","
                + "\"timestamps\":{\"start\":" + startTimestamp + "},"
                + "\"assets\":{"
                + "\"large_image\":\"" + largeKey + "\","
                + "\"large_text\":\"catogod.cc\","
                + "\"small_image\":\"cop26logo_cropped_1_\","
                + "\"small_text\":\"" + jsonEscape("Version: " + IdkHackMod.VERSION) + "\""
                + "}"
                + "}";
    }

    // -------------------------------------------------------------------------
    // IPC frame I/O
    // -------------------------------------------------------------------------

    private static void writeFrame(int opcode, String json) throws IOException {
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buf = ByteBuffer.allocate(8 + body.length).order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(opcode);
        buf.putInt(body.length);
        buf.put(body);
        buf.flip();
        while (buf.hasRemaining()) socket.write(buf);
    }

    private static String readFrame() throws IOException {
        ByteBuffer header = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        while (header.hasRemaining()) {
            if (socket.read(header) == -1) throw new IOException("Socket closed");
        }
        header.flip();
        header.getInt(); // opcode (ignored)
        int length = header.getInt();

        ByteBuffer body = ByteBuffer.allocate(length);
        while (body.hasRemaining()) {
            if (socket.read(body) == -1) throw new IOException("Socket closed");
        }
        return new String(body.array(), StandardCharsets.UTF_8);
    }

    // -------------------------------------------------------------------------
    // Socket discovery
    // -------------------------------------------------------------------------

    private static Path findIpcSocket() {
        String[] bases = {
                System.getenv("XDG_RUNTIME_DIR"),
                System.getenv("TMPDIR"),
                "/run/user/" + getUid(),
                "/tmp"
        };
        for (String base : bases) {
            if (base == null) continue;
            for (int i = 0; i <= 9; i++) {
                Path p = Path.of(base, "discord-ipc-" + i);
                if (p.toFile().exists()) return p;
            }
        }
        return null;
    }

    private static String getUid() {
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"id", "-u"});
            return new String(p.getInputStream().readAllBytes()).trim();
        } catch (Exception e) {
            return "1000";
        }
    }

    // -------------------------------------------------------------------------
    // Presence text helpers (kept from original)
    // -------------------------------------------------------------------------

    public static String getState() {
        String playing = mc.player == null
                ? "In the menus"
                : mc.isIntegratedServerRunning()
                ? "Lonely in singleplayer"
                : "Playing multiplayer";
        return playing + ".";
    }

    public static String getImageKey() {
        if (RPC.INSTANCE == null) return "cop26logo_cropped_1_";
        switch (RPC.INSTANCE.image.getValue()) {
            case "Animals":
                return switch (MathUtil.randomInt(1, 7)) {
                    case 1 -> "cato";
                    case 2 -> "samoyed2";
                    case 3 -> "laptopcat";
                    case 4 -> "moneycat";
                    case 5 -> "moneycat2";
                    case 6 -> "moneycat3";
                    default -> "samoyed1";
                };
            case "idkIcon":
                return "cop26logo_cropped_1_";
            case "Grails":
                return switch (MathUtil.randomInt(1, 6)) {
                    case 1 -> "lelcopter";
                    case 2 -> "nfttim";
                    case 3 -> "phonto";
                    case 4 -> "lole1";
                    case 5 -> "grail1";
                    default -> "grail2";
                };
            default:
                return "cop26logo_cropped_1_";
        }
    }

    private static String jsonEscape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
