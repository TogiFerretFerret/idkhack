package sh.idktheflag.idk.impl.features.commands;

import sh.idktheflag.idk.api.command.Command;
import sh.idktheflag.idk.api.friends.Friend;
import sh.idktheflag.idk.api.management.FriendManager;
import sh.idktheflag.idk.api.utils.chat.ChatMessage;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;

public class FriendCommand extends Command {
    public FriendCommand()
    {
        super("Friend", "friend kids", new String[]{"friend"});
    }

    @Override
    public void run(String[] args)
    {
        if (args.length > 2)
        {
            if (args[1].equalsIgnoreCase("add"))
            {
                FriendManager.INSTANCE.getFriends().add(new Friend(args[2]));

                ChatUtils.sendMessage(new ChatMessage(
                        "Added friend with ign: " + args[2],
                        false,
                        0
                ));
            } else if (args[1].equalsIgnoreCase("del"))
            {
                FriendManager.INSTANCE.getFriends().removeIf(value -> value.toString().equalsIgnoreCase(new Friend(args[2]).toString()));
                ChatUtils.sendMessage(new ChatMessage(
                        "Removed friend with ign: " + args[2],
                        false,
                        0
                ));
            } else
            {
                ChatUtils.sendMessage(new ChatMessage(
                        "Invalid format",
                        false,
                        0
                ));
            }
        } else
        {
            ChatUtils.sendMessage(new ChatMessage(
                    "Invalid format",
                    false,
                    0
            ));
        }
    }


    @Override
    public String[] getFill(String[] args)
    {
        if (args.length > 1)
        {
            if (args[1].equalsIgnoreCase("add"))
            {
                return new String[]{"{add,del}", "{NONFRIENDS}"};
            } else if (args[1].equalsIgnoreCase("del"))
            {
                return new String[]{"{add,del}", "{FRIENDS}"};
            }
        }
        return new String[]{"{add,del}", "player"};
    }
}
