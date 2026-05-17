package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import net.minecraft.util.math.MathHelper;

public class Yaw extends Module {


    public static Yaw INSTANCE;

    public Yaw()
    {
        super("Yaw", Category.Player);
        INSTANCE = this;
    }


    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck())
        {
            return;
        }

        float diff = 360 / 8f;
        float yaw = mc.player.getYaw() + 180F;
        yaw = Math.round((yaw / diff)) * diff;
        yaw -= 180F;
        mc.player.setYaw(yaw);
    }

    @Override
    public String getHudInfo()
    {
        if (NullUtils.nullCheck())
        {
            return null;
        }
        return MathHelper.floor(MathHelper.wrapDegrees(mc.player.getYaw())) + "";
    }

    @Override
    public String getDescription()
    {
        return "Yaw: Locks your yaw to go straight in one direction";
    }


}
