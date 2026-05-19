package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.api.event.events.render.RenderCrystalEvent;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.model.EndCrystalEntityModel;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCrystalEntityRenderer.class)
public class MixinEndCrystalEntityRenderer {

    @Shadow
    @Final
    private EndCrystalEntityModel model;

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "HEAD"), cancellable = true)
    private void hookRender(EndCrystalEntityRenderState state, MatrixStack matrixStack,
                            OrderedRenderCommandQueue vertexConsumers, CameraRenderState cameraRenderState,
                            CallbackInfo ci) {
        RenderCrystalEvent event = new RenderCrystalEvent(state, matrixStack, model);
        event.post();
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
