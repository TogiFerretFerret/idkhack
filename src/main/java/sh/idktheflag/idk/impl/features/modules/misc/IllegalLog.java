package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.feature.module.Module;

public class IllegalLog extends Module
{
    public static IllegalLog INSTANCE;

    public IllegalLog()
    {
        super("IllegalLog", Category.Misc);
        INSTANCE = this;
    }


    @Override
    public String getDescription()
    {
        return "IllegalLog: ensures you get disconnected from the server when you log";
    }
}