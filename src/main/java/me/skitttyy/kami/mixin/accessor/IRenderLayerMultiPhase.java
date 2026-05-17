package me.skitttyy.kami.mixin.accessor;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// RenderLayer.MultiPhase inner class is removed in 1.21.11
// RenderLayer is now a simple class without inner MultiPhase/MultiPhaseParameters
// Render setup is now handled by RenderSetup class
@Mixin(RenderLayer.class)
public interface IRenderLayerMultiPhase
{

}
