package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.PacketManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;

public class NoAnim extends Module {

    public NoAnim()
    {
        super("NoAnim", Category.Misc);
    }

    Value<Boolean> noMineAnim = new ValueBuilder<Boolean>()
            .withDescriptor("No Mine Anim")
            .withValue(false)
            .register(this);
    Value<Boolean> noBob = new ValueBuilder<Boolean>()
            .withDescriptor("No Bob")
            .withValue(false)
            .register(this);
    Value<Boolean> noSwing = new ValueBuilder<Boolean>()
            .withDescriptor("No Swing")
            .withValue(false)
            .register(this);


    @SubscribeEvent
    public void onPostPacket(PacketEvent.SendPost event)
    {
        if (event.getPacket() instanceof PlayerActionC2SPacket packet && noMineAnim.getValue())
        {

            if (PacketManager.INSTANCE.isCached(packet)) return;


            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK)
            {

                PacketManager.INSTANCE.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, packet.getPos(), packet.getDirection(), packet.getSequence()));

            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (event.getPacket() instanceof HandSwingC2SPacket && noSwing.getValue())
        {
            event.setCancelled(true);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;

        if (noBob.getValue())
            mc.player.distanceTraveled = 4.0f;
    }

    @Override
    public String getDescription()
    {
        return "NoAnim: Hides various animations";
    }
}
