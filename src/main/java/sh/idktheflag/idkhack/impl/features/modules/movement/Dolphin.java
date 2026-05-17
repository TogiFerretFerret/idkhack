package sh.idktheflag.idkhack.impl.features.modules.movement;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.utils.Timer;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;

public class Dolphin extends Module {

    public static Dolphin INSTANCE;

    public Dolphin()
    {
        super("Dolphin", Category.Movement);
        INSTANCE = this;
    }

    Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;

        timer.setDelay(400L);
        if ((mc.player.isSubmergedInWater() || mc.player.isInLava()))
        {
            if (timer.isPassed())
            {
                if (mc.player.isSneaking())
                {
                    PlayerUtils.setMotionY(-0.1);
                } else if (mc.options.jumpKey.isPressed())
                {
                    PlayerUtils.setMotionY(0.09);
                }
            }
        } else
        {
            timer.resetDelay();
        }
    }


    @Override
    public String getHudInfo()
    {
        return "AAC";
    }

    @Override
    public String getDescription()
    {
        return "Dolphin: Swim like a dolphin in water (dive down and up)";
    }
}
