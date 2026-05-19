package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.impl.features.modules.render.NoRender;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.EnchantingTableBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EnchantingTableBlockEntityRenderer.class)
public class MixinEnchantingTableBlockEntityRenderer {

    @Inject(method = "render(Lnet/minecraft/client/render/block/entity/state/EnchantingTableBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At("HEAD"), cancellable = true)
    private void hookRender(EnchantingTableBlockEntityRenderState state, MatrixStack matrixStack,
                            OrderedRenderCommandQueue queue, CameraRenderState cameraState, CallbackInfo ci)
    {
        if (NoRender.INSTANCE.enchantmentTable.getValue() && NoRender.INSTANCE.isEnabled())
        {
            ci.cancel();
        }
    }
}
