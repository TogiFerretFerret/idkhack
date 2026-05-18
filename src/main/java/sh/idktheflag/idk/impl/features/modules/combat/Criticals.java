package sh.idktheflag.idk.impl.features.modules.combat;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.PacketManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.world.PacketUtils;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Criticals extends Module {

    public Criticals() {
        super("Criticals", Category.Combat);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (NullUtils.nullCheck()) return;
        if (!(event.getPacket() instanceof PlayerInteractEntityC2SPacket)) return;
        if (PacketUtils.getInteractType((PlayerInteractEntityC2SPacket) event.getPacket()) != PacketUtils.InteractType.ATTACK) return;
        if (!mc.player.isOnGround()) return;
        if (mc.player.isTouchingWater() || mc.player.isInLava()) return;

        double x = mc.player.getX(), y = mc.player.getY(), z = mc.player.getZ();
        float yaw = mc.player.getYaw(), pitch = mc.player.getPitch();

        PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.Full(x, y + 0.0625, z, yaw, pitch, false, false));
        PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, false, false));
    }

    @Override
    public String getDescription() {
        return "Criticals: force critical hits by faking a small jump before each attack";
    }
}
