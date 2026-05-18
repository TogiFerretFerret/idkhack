package sh.idktheflag.idk.api.utils;

import sh.idktheflag.idk.api.wrapper.IMinecraft;

public class NullUtils implements IMinecraft {

    public static boolean nullCheck()
    {
        return (mc.player == null || mc.world == null || mc.interactionManager == null);
    }
}
