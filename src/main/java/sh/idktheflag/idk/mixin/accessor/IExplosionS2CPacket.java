package sh.idktheflag.idk.mixin.accessor;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;

// TODO: 1.21.11 - playerVelocityX/Y/Z replaced with Optional<Vec3d> playerKnockback
@Mixin(ExplosionS2CPacket.class)
public interface IExplosionS2CPacket {
}
