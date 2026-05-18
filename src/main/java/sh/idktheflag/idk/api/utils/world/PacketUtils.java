package sh.idktheflag.idk.api.utils.world;

import io.netty.buffer.Unpooled;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class PacketUtils implements IMinecraft {
    public static InteractType getInteractType(PlayerInteractEntityC2SPacket packet)
    {
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.write(packetBuf);

        packetBuf.readVarInt();
        return packetBuf.readEnumConstant(InteractType.class);
    }

    public static Entity getEntity(PlayerInteractEntityC2SPacket packet)
    {
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.write(packetBuf);
        return mc.world.getEntityById(packetBuf.readVarInt());
    }
    public enum InteractType {
        INTERACT,
        ATTACK,
        INTERACT_AT
    }


}
