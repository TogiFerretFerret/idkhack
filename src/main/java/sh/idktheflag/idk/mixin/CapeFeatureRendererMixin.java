package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.impl.features.modules.render.Capes;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(MatrixStack matrices, OrderedRenderCommandQueue vertexConsumers, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance, CallbackInfo ci) {
        if (Capes.INSTANCE.isEnabled()) {
            // Logic to use custom cape texture would go here
        }
    }
}
