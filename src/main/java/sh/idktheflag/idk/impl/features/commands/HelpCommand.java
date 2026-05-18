package sh.idktheflag.idk.impl.features.commands;

import sh.idktheflag.idk.api.command.Command;
import sh.idktheflag.idk.api.management.CommandManager;
import sh.idktheflag.idk.api.utils.chat.ChatMessage;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("Help", "shows you all the commands", new String[]{"help", "commands"});
    }

    @Override
    public void run(String[] args) {
        for (Command command : CommandManager.INSTANCE.getCommands()){
            ChatUtils.sendMessage(new ChatMessage(
                    command.getName() + " - " + command.getDesc(),
                    false,
                    0
            ));
        }
    }
}
