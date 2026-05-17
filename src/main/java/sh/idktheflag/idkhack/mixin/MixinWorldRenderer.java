package sh.idktheflag.idkhack.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// WorldRenderer.render signature changed completely:
//   Old: render(RenderTickCounter, boolean, Camera, GameRenderer, LightmapTextureManager, Matrix4f, Matrix4f)
//   New: render(ObjectAllocator, RenderTickCounter, boolean, Camera, Matrix4f, Matrix4f, Matrix4f, GpuBufferSlice, Vector4f, boolean)
// GameRenderer and LightmapTextureManager are no longer passed to render
// setupTerrain method is removed
// EntityRenderDispatcher was renamed to EntityRenderManager
@Mixin(WorldRenderer.class)
public class MixinWorldRenderer
{

}
