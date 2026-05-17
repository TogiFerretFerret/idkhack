package sh.idktheflag.idkhack.mixin.accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LimbAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LimbAnimator.class)
public interface ILimbAnimator {
    @Accessor("pos")
    void setLimbPos(float i);
}
