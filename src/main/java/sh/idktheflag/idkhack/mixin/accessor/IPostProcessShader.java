package sh.idktheflag.idkhack.mixin.accessor;

import net.minecraft.client.gl.PostEffectPass;
import org.spongepowered.asm.mixin.Mixin;

// TODO: 1.21.11 - input/output Framebuffer fields removed from PostEffectPass
@Mixin(PostEffectPass.class)
public interface IPostProcessShader {
}
