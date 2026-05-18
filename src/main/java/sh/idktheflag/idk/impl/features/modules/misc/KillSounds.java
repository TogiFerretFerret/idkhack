package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.LivingEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.SoundManager;
import sh.idktheflag.idk.api.management.notification.NotificationManager;
import sh.idktheflag.idk.api.management.notification.types.TopNotification;
import sh.idktheflag.idk.impl.features.modules.client.Manager;
import sh.idktheflag.idk.impl.features.modules.combat.CatAura;
import sh.idktheflag.idk.impl.features.modules.combat.KillAura;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.Random;

public class KillSounds extends Module
{


    public KillSounds()
    {
        super("KillSounds", Category.Misc);
    }

    @SubscribeEvent
    private void onDeath(LivingEvent.Death event)
    {
        if (!(event.getEntity() instanceof PlayerEntity)) return;

        if (!(event.getEntity() == mc.player))
        {
            SoundManager.INSTANCE.play(SoundManager.INSTANCE.KILL_SOUND);
        }
    }

    @Override
    public String getDescription()
    {
        return "KillSounds: plays a sound when you kill someone";
    }
}
