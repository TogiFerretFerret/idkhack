package sh.idktheflag.idkhack.mixin.accessor;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;

// TODO: 1.21.11 - yaw/pitch fields removed, now uses EntityPosition change
@Mixin(PlayerPositionLookS2CPacket.class)
public interface IPlayerPositionLookS2CPacket {
}
