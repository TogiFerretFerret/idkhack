package sh.idktheflag.idkhack.impl.features.commands;

import sh.idktheflag.idkhack.api.command.Command;
import sh.idktheflag.idkhack.api.management.CommandManager;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import sh.idktheflag.idkhack.impl.features.modules.misc.Spammer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SpammerCommand extends Command {
    String lastFile = "";


    public SpammerCommand()
    {
        super("Spammer", "spam kids dox", new String[]{"spammer"});
    }

    @Override
    public void run(String[] args)
    {
        boolean isIncorrect = isIncorrect(args);

        if (isIncorrect)
        {
            handleIncorrectUsage();
            return;
        }
        if (Objects.equals(args[1], "folder"))
        {
            openDirectory();
        } else if (Objects.equals(args[1], "load"))
        {
            if (args.length == 3)
            {
                handleSetFile(args);
            } else
            {
                ChatUtils.sendMessage(Formatting.RED + "Incorrect usage: spammer load <filename>");
            }
        } else if (Objects.equals(args[1], "file"))
        {
            if (Objects.equals(lastFile, ""))
            {
                ChatUtils.sendMessage(Formatting.RED + "No spammer loaded! use spammer load <filename> to load a spammer!");
            } else
            {
                ChatUtils.sendMessage(Formatting.AQUA + "[Spammer]" + Formatting.BLUE + " Spammer file " + lastFile + " is loaded.");
            }
        }
    }

    private static void openDirectory()
    {
        File spammerFile = new File(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), File.separator + "idkhack" + File.separator + "spammer" + File.separator);

        if (!spammerFile.exists()) spammerFile.mkdir();

        Util.getOperatingSystem().open(spammerFile);
    }

    private void handleSetFile(String[] args)
    {
        File spammerFile = new File(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), File.separator + "idkhack" + File.separator + "spammer" + File.separator + args[2]);
        if (!spammerFile.exists())
        {
            ChatUtils.sendMessage(Formatting.AQUA + "[Spammer]" + Formatting.BLUE + " File does not exist!");
        } else
        {
            ArrayList<String> spammerMessages = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(spammerFile)))
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    spammerMessages.add(line);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            lastFile = spammerFile.getName();
            ChatUtils.sendMessage(Formatting.AQUA + "[Spammer]" + Formatting.BLUE + " Loaded " + spammerFile.getName() + "!");
            Spammer.INSTANCE.spammerText = spammerMessages;
        }
    }

    private void handleIncorrectUsage()
    {
        ChatUtils.sendMessage(Formatting.RED + "Incorrect usage!");
        ChatUtils.sendMessage(Formatting.AQUA + "Usage: " + Formatting.WHITE + CommandManager.INSTANCE.PREFIX + "spammer <filename|dir>");
    }

    private boolean isIncorrect(String[] args)
    {
        boolean incorrect = false;
        if (args.length < 2)
        {
            incorrect = true;
        }
        return incorrect;
    }

    @Override
    public String[] getFill(String[] args)
    {
        return new String[]{"{file,folder,load}", "{SPAMMERFILE}"};
    }

}
