package sh.idktheflag.idk.impl.features.commands;

import sh.idktheflag.idk.api.command.Command;
import sh.idktheflag.idk.api.management.PopManager;
import sh.idktheflag.idk.api.utils.chat.ChatMessage;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import net.minecraft.util.Formatting;

public class ClearPopsCommand extends Command {
    public ClearPopsCommand()
    {
        super("ClearPops", "Clears pops of a player", new String[]{"clearpops"});
    }

    @Override
    public void run(String[] args)
    {
        if (args.length == 2)
        {
            if (!PopManager.INSTANCE.registry.containsKey(args[1]))
            {
                ChatUtils.sendMessage(new ChatMessage(
                        Formatting.RED + args[1] + " has not popped a single time!",
                        false,
                        0
                ));
            } else
            {
                PopManager.INSTANCE.clearPops(args[1]);
                ChatUtils.sendMessage(new ChatMessage(
                        Formatting.AQUA + "Reset " + args[1] + " to 0 pops!",
                        false,
                        0
                ));
            }
        } else
        {
            ChatUtils.sendMessage(new ChatMessage(
                    "Please input a real player",
                    false,
                    0
            ));
        }
    }

    @Override
    public String[] getFill(String[] args)
    {
        return new String[]{"{PLAYERS}"};
    }
}
