package sh.idktheflag.idk.api.utils.render.world.layer;

import sh.idktheflag.idk.api.wrapper.IMinecraft;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;

// TODO: port to 1.21.11 - RenderPhase and its inner classes (DepthTest, Texture, etc.)
// have been completely removed in 1.21.11, replaced by RenderSetup and RenderPipeline.
// RenderLayer.of() now takes RenderSetup instead of MultiPhaseParameters.
// The ENCHANT layer needs to be recreated using the new pipeline system.
public class IdkLayers implements IMinecraft {
    // TODO: port to 1.21.11 - Recreate custom ENCHANT RenderLayer using new RenderSetup/RenderPipeline API
    // Using RenderLayers.glint() as fallback
    public static final RenderLayer ENCHANT = RenderLayers.glint();
}
