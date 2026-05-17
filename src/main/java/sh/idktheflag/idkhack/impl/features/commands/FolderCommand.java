package sh.idktheflag.idkhack.impl.features.commands;

import sh.idktheflag.idkhack.api.command.Command;
import sh.idktheflag.idkhack.api.management.SavableManager;
import net.minecraft.util.Util;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FolderCommand extends Command {
    public FolderCommand()
    {
        super("Folder", "opens Sn0w folder", new String[]{"folder"});
    }

    @Override
    public void run(String[] args)
    {
        File dir = new File(SavableManager.MAIN_FOLDER.getAbsolutePath());
        if (!dir.exists()) dir.mkdir();

        Util.getOperatingSystem().open(SavableManager.MAIN_FOLDER.getAbsoluteFile());
    }
}
