package sh.idktheflag.idkhack.mixin;

import net.minecraft.client.gl.ShaderProgram;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// JsonEffectShaderProgram and ShaderStage are removed in 1.21.11
// The shader system was completely rewritten - now uses ShaderLoader, ShaderProgram, CompiledShader, etc.
// ShaderStage.Type is gone entirely
@Mixin(ShaderProgram.class)
public class MixinJsonEffectGlShader
{

}
