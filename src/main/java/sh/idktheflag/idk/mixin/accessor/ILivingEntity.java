package sh.idktheflag.idk.mixin.accessor;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface ILivingEntity {
    // lastAttackedTicks was removed in 1.21.11

    @Accessor("jumpingCooldown")
    int getLastJumpCooldown();

    @Accessor("jumpingCooldown")
    void setLastJumpCooldown(int val);
}
