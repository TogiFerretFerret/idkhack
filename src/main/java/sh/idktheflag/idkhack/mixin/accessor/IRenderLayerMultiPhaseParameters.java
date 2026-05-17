package sh.idktheflag.idkhack.mixin.accessor;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// RenderLayer.MultiPhaseParameters inner class is removed in 1.21.11
// OutlineMode is now at RenderSetup.OutlineMode
// Texture info is now in RenderSetup.TextureSpec
@Mixin(RenderLayer.class)
public interface IRenderLayerMultiPhaseParameters
{

}
