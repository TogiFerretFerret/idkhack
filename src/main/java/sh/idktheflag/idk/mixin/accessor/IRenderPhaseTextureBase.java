package sh.idktheflag.idk.mixin.accessor;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// RenderPhase.TextureBase is removed in 1.21.11
// RenderPhase class itself is gone - replaced by RenderSetup
// Texture info is now accessed via RenderSetup.TextureSpec which has a location() method
@Mixin(RenderLayer.class)
public interface IRenderPhaseTextureBase
{

}
