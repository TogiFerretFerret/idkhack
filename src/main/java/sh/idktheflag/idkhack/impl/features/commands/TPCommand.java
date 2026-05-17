package sh.idktheflag.idkhack.impl.features.commands;

import sh.idktheflag.idkhack.api.command.Command;
import sh.idktheflag.idkhack.api.utils.chat.ChatMessage;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;

public class TPCommand extends Command {
    public TPCommand() {
        super("Tp", "teleports you to x y z", new String[]{"tp"});
    }

    @Override
    public void run(String[] args) {
        if (args.length == 4){
            mc.player.setPosition(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        } else {
            ChatUtils.sendMessage(new ChatMessage(
                    "Please input a valid command",
                    false,
                    0
            ));
        }
    }


    @Override
    public String[] getFill(String[] args)
    {
        return new String[]{"x", "y", "z"};
    }

}
