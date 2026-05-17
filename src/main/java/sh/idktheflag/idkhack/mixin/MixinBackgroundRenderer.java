package sh.idktheflag.idkhack.mixin;

import net.minecraft.client.render.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// BackgroundRenderer was moved to net.minecraft.client.render.fog.FogRenderer
// applyFog signature changed: now returns Vector4f and takes (Camera, int, RenderTickCounter, float, ClientWorld)
// RenderSystem.setShaderFogStart/End/Color are removed, fog is now handled via GpuBufferSlice
// FogType values changed: FOG_TERRAIN -> WORLD, FOG_SKY -> NONE
@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {

}
