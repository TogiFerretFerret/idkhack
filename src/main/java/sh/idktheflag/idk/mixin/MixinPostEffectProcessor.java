package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.api.utils.ducks.IPostEffectProcessor;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(PostEffectProcessor.class)
public class MixinPostEffectProcessor implements IPostEffectProcessor {

    @Shadow @Final
    private Map<Identifier, Framebuffer> framebuffers;

    @Override
    public void overwriteBuffer(String name, Framebuffer buffer) {
        framebuffers.put(Identifier.ofVanilla(name), buffer);
    }
}
