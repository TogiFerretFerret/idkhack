package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.world.PacketUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;


public class EntityControl extends Module {
    public static EntityControl INSTANCE;

    public EntityControl()
    {
        super("EntityControl", Category.Player);
        INSTANCE = this;
    }

    Value<Boolean> mountBypass = new ValueBuilder<Boolean>()
            .withDescriptor("Mount Bypass")
            .withValue(false)
            .register(this);


    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (NullUtils.nullCheck()) return;
        if (mountBypass.getValue())
        {
            if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet)
            {
                if (PacketUtils.getInteractType(packet) == PacketUtils.InteractType.INTERACT_AT && PacketUtils.getEntity(packet) instanceof AbstractDonkeyEntity)
                    event.setCancelled(true);
            }
        }
    }


    @Override
    public String getHudInfo()
    {
        if (mountBypass.getValue())
            return "Mount";

        return "Ride";
    }


    @Override
    public String getDescription()
    {
        return "EntityControl: Control entities u ride";
    }
}