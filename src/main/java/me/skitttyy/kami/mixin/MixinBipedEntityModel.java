package me.skitttyy.kami.mixin;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// BipedEntityModel now extends EntityModel<BipedEntityRenderState> instead of AnimalModel<LivingEntity>
// setAngles now takes (BipedEntityRenderState) instead of (entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch)
// getHeadParts() and getBodyParts() are removed
// The redirect on getFallFlyingTicks won't work since setAngles no longer references the entity directly
@Mixin(BipedEntityModel.class)
public class MixinBipedEntityModel {

}
