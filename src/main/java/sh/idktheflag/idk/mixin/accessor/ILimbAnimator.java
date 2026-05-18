package sh.idktheflag.idk.mixin.accessor;

import net.minecraft.entity.LimbAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LimbAnimator.class)
public interface ILimbAnimator {
    @Accessor("animationProgress")
    void setLimbPos(float i);
}
