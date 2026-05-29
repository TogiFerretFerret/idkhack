package sh.idktheflag.idk.mixin.accessor;

import net.minecraft.entity.EntityPosition;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerPositionLookS2CPacket.class)
public interface IPlayerPositionLookS2CPacket {
    @Accessor("change")
    EntityPosition getChange();
}
