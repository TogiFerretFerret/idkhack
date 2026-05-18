package sh.idktheflag.idk.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sh.idktheflag.idk.impl.features.modules.render.Capes;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    // TODO: port to 1.21.11 - cape rendering method signature changed
}
