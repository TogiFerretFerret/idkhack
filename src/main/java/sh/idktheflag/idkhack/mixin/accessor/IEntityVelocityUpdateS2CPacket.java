package sh.idktheflag.idkhack.mixin.accessor;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;

// TODO: 1.21.11 - velocityX/Y/Z fields removed (packet is now a record)
@Mixin(EntityVelocityUpdateS2CPacket.class)
public interface IEntityVelocityUpdateS2CPacket {
}
