package sh.idktheflag.idk.impl.features.commands;

import sh.idktheflag.idk.api.command.Command;
import sh.idktheflag.idk.api.utils.chat.ChatMessage;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class GrabCommand extends Command {
    public GrabCommand() {
        super("Grab Coords", "copys your coordinates to clipboard", new String[]{"grab"});
    }

    @Override
    public void run(String[] args) {
        String coords = "X: " +  (int) mc.player.getX() + " Y: " + (int) mc.player.getY() + " Z: " + (int)  mc.player.getZ();
        StringSelection selection = new StringSelection(coords);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        ChatUtils.sendMessage(new ChatMessage(
                "Copied Coords to clipboard",
                false,
                0
        ));
    }
}
