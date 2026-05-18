package sh.idktheflag.idk.mixin.accessor;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface ILivingEntity {
    // TODO: 1.21.11 - lastAttackedTicks removed
    // int getLastAttackedTicks();
    // void setLastAttackedTicks(int ticks);

    // TODO: 1.21.11 - check if jumpingCooldown still exists
    // @Accessor("jumpingCooldown")
    // int getLastJumpCooldown();
    // @Accessor("jumpingCooldown")
    // void setLastJumpCooldown(int val);

}