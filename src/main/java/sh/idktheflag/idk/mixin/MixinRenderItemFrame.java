package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.features.modules.render.NoRender;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.entity.state.ItemFrameEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntityRenderer.class)
public class MixinRenderItemFrame<T extends ItemFrameEntity> implements IMinecraft
{
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/ItemFrameEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void doRender(ItemFrameEntityRenderState state, MatrixStack matrixStack,
                         OrderedRenderCommandQueue vertexConsumers, CameraRenderState cameraRenderState,
                         CallbackInfo ci)
    {
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.itemFrame.getValue())
        {
            // squaredDistanceToCamera < 36.0 is equivalent to distance < 6.0
            if (mc.player != null && state.squaredDistanceToCamera < 36.0) return;
            ci.cancel();
        }
    }
}
