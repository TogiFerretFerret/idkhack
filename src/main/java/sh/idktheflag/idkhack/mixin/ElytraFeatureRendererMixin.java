package sh.idktheflag.idkhack.mixin;

import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11 - ElytraFeatureRenderer rendering API changed
@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraFeatureRendererMixin {
}
