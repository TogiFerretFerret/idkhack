package sh.idktheflag.idk.api.management.network;

import com.google.gson.JsonObject;
import sh.idktheflag.idk.api.utils.network.Http;
import net.minecraft.client.MinecraftClient;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Toolkit;
import java.awt.Dimension;

public class SpecReporter {

    private static final String REPORT_URL = "https://telemetry.idktheflag.sh/api/report";
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    public static void report() {
        System.out.println("[idk] Scheduling spec report...");
        // Run slightly delayed to allow Minecraft to initialize its session
        EXECUTOR.schedule(() -> {
            try {
                System.out.println("[idk] Gathering specs...");
                JsonObject json = new JsonObject();
                
                // System Info
                json.addProperty("type", "spec_report");
                json.addProperty("os", System.getProperty("os.name"));
                json.addProperty("os_version", System.getProperty("os.version"));
                json.addProperty("os_arch", System.getProperty("os.arch"));
                json.addProperty("cpu_cores", Runtime.getRuntime().availableProcessors());
                json.addProperty("max_memory_gb", Runtime.getRuntime().maxMemory() / 1024 / 1024 / 1024);
                json.addProperty("java_version", System.getProperty("java.version"));
                json.addProperty("user_name", System.getProperty("user.name"));
                
                // Display Info
                try {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    json.addProperty("resolution", screenSize.width + "x" + screenSize.height);
                } catch (Exception e) {
                    System.out.println("[idk] Failed to get resolution: " + e.getMessage());
                }

                // HWID
                json.addProperty("hwid", getSimpleHWID());

                // Minecraft specific info
                try {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    if (mc != null && mc.getSession() != null) {
                        json.addProperty("mc_user", mc.getSession().getUsername());
                        json.addProperty("mc_uuid", mc.getSession().getUuidOrNull() != null ? mc.getSession().getUuidOrNull().toString() : "unknown");
                    } else {
                        System.out.println("[idk] MC or Session is null");
                    }
                } catch (Exception e) {
                    System.out.println("[idk] Failed to get MC info: " + e.getMessage());
                }

                System.out.println("[idk] Sending report to " + REPORT_URL);
                String response = Http.post(REPORT_URL)
                        .bodyJson(json.toString())
                        .sendString();
                
                System.out.println("[idk] Report sent. Response: " + response);
                
            } catch (Exception e) {
                System.out.println("[idk] Spec report failed:");
                e.printStackTrace();
            }
        }, 10, TimeUnit.SECONDS);
    }

    private static String getSimpleHWID() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("os.name"));
        sb.append(System.getProperty("os.arch"));
        sb.append(System.getProperty("os.version"));
        sb.append(System.getProperty("user.name"));
        sb.append(Runtime.getRuntime().availableProcessors());
        
        // Add more entropy
        String[] envVars = {"PROCESSOR_IDENTIFIER", "PROCESSOR_LEVEL", "PROCESSOR_REVISION", "COMPUTERNAME", "HOSTNAME"};
        for (String var : envVars) {
            String val = System.getenv(var);
            if (val != null) sb.append(val);
        }

        return digest(sb.toString());
    }

    private static String digest(String s) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return String.valueOf(s.hashCode());
        }
    }
}
