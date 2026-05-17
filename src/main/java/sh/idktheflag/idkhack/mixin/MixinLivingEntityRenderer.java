package sh.idktheflag.idkhack.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// LivingEntityRenderer now takes 3 type params: <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
// render() now takes (S renderState, MatrixStack, OrderedRenderCommandQueue, CameraRenderState) instead of (entity, float, float, MatrixStack, VertexConsumerProvider, int)
// The render method no longer receives the entity directly - it receives a pre-computed render state
// MathHelper.lerpAngleDegrees redirects on body yaw/head yaw won't work since render uses render states
// getRenderLayer now takes (S, boolean, boolean, boolean) with S being render state
// setupTransforms now takes (S, MatrixStack, float, float)
// FeatureRenderer type params changed
@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer
{

}
