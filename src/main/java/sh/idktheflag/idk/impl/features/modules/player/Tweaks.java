package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.mixin.accessor.ILivingEntity;
import net.minecraft.entity.EntityPose;

public class Tweaks extends Module {
    Value<Boolean> fastJump = new ValueBuilder<Boolean>()
            .withDescriptor("Fast Jump")
            .withValue(false)
            .register(this);
    public Value<Boolean> noCrawl = new ValueBuilder<Boolean>()
            .withDescriptor("No Crawl")
            .withValue(false)
            .register(this);
    public Value<Boolean> crouch = new ValueBuilder<Boolean>()
            .withDescriptor("Crouch")
            .withValue(false)
            .register(this);

    public Value<Boolean> stealButton = new ValueBuilder<Boolean>()
            .withDescriptor("Steal Button")
            .withValue(false)
            .register(this);
    public Value<Boolean> regearButton = new ValueBuilder<Boolean>()
            .withDescriptor("Regear Button")
            .withValue(false)
            .register(this);
    public static Tweaks INSTANCE;

    public Tweaks()
    {
        super("Tweaks", Category.Player);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;


// TODO 1.21.11:         if (fastJump.getValue() && ((ILivingEntity) mc.player).getLastJumpCooldown() > 0)
// TODO 1.21.11:             ((ILivingEntity) mc.player).setLastJumpCooldown(0);

        if (noCrawl.getValue() && mc.player.getPose().equals(EntityPose.SWIMMING))
        {
            mc.player.setPose(EntityPose.STANDING);
        }


    }

    @Override
    public String getDescription()
    {
        return "Tweaks: Player tweaks, has stuff like fastjump";
    }
}
