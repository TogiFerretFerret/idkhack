package sh.idktheflag.idkhack.impl.features.modules.movement;

import sh.idktheflag.idkhack.api.event.eventbus.Priority;
import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.move.MovementPacketsEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.PacketManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;


public class Disabler extends Module
{
    public static Disabler INSTANCE;


    public Disabler()
    {
        super("Disabler", Category.Player);
        INSTANCE = this;
    }

    public Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Overflow")
            .withModes("Overflow", "GrimV3")
            .register(this);


    @SubscribeEvent(Priority.SUPER_FIRST)
    public void onPacket(PacketEvent.Send event)
    {
        if (NullUtils.nullCheck()) return;


        if (mode.getValue().equals("GrimV3"))
        {
            if (event.getPacket() instanceof PlayerActionC2SPacket packet
                    && packet.getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK)
            {
                PacketManager.INSTANCE.sendPacket(new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, packet.getPos(), packet.getDirection()));
            }
        }
    }

    @SubscribeEvent
    public void onMovementPackets(MovementPacketsEvent event)
    {
        if (mode.getValue().equals("Overflow"))
        {
            event.setCancelled(true);
            event.setYaw(1.0E10f);
        }


    }

    @Override
    public String getHudInfo()
    {
        return mode.getValue();
    }

    @Override
    public String getDescription()
    {
        return "Disabler: disables anticheats";
    }
}
