package me.skitttyy.kami.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// PlayerEntityRenderer now extends LivingEntityRenderer<AvatarlikeEntity, PlayerEntityRenderState, PlayerEntityModel>
// setupTransforms now takes (PlayerEntityRenderState, MatrixStack, float, float) instead of (AbstractClientPlayerEntity, MatrixStack, float, float, float, float)
// getTexture now takes PlayerEntityRenderState instead of AbstractClientPlayerEntity
// render() uses OrderedRenderCommandQueue + CameraRenderState instead of VertexConsumerProvider + int
// The entity is no longer directly accessible in render methods - must use render states
@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer
{

}
