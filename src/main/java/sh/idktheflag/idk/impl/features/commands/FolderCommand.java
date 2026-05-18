package sh.idktheflag.idk.impl.features.commands;

import sh.idktheflag.idk.api.command.Command;
import sh.idktheflag.idk.api.management.SavableManager;
import net.minecraft.util.Util;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FolderCommand extends Command {
    public FolderCommand()
    {
        super("Folder", "opens idk folder", new String[]{"folder"});
    }

    @Override
    public void run(String[] args)
    {
        File dir = new File(SavableManager.MAIN_FOLDER.getAbsolutePath());
        if (!dir.exists()) dir.mkdir();

        Util.getOperatingSystem().open(SavableManager.MAIN_FOLDER.getAbsoluteFile());
    }
}
