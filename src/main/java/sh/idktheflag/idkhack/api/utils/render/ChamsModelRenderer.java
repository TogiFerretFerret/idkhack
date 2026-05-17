package sh.idktheflag.idkhack.api.utils.render;

// TODO: port to 1.21.11 - The entire ChamsModelRenderer needs to be rewritten.
// Major API changes in 1.21.11 affecting this file:
// - EntityModel no longer has type parameters compatible with LivingEntity casting
// - PlayerEntityModel no longer takes generic parameters
// - LivingEntityRenderer.shouldFlipUpsideDown() is no longer static
// - model.animateModel(), model.setAngles() signatures changed
// - LimbAnimator.getSpeed()/getPos() signatures changed
// - renderer.setupTransforms(), renderer.scale() signatures changed
// - AnimalModel fields (child, headScaled, invertedChildHeadScale, etc.) removed
// - SinglePartEntityModel, CompositeEntityModel, LlamaEntityModel, RabbitEntityModel API changed
// - PlayerEntityRenderer.getArmPose() now takes Arm instead of Hand
// - PlayerEntityRenderer.setModelPose() removed
// - RenderSystem.disableDepthTest() removed
// - Entity.isGliding(), getFallFlyingTicks(), lerpVelocity() etc. changed
// - Various model field access patterns changed (prevBodyYaw -> lastBodyYaw, etc.)
// - mc.gameRenderer.getBasicProjectionMatrix() signature changed
// - firstPersonRenderer field access changed

import com.google.common.base.MoreObjects;
import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4f;

import java.awt.*;

public class ChamsModelRenderer implements IMinecraft
{
    private static final float SINE_45_DEGREES = (float) Math.sin(0.7853981633974483);
    private static final MatrixStack matrices = new MatrixStack();
    private static final Vector4f pos1 = new Vector4f();
    private static final Vector4f pos2 = new Vector4f();
    private static final Vector4f pos3 = new Vector4f();
    private static final Vector4f pos4 = new Vector4f();

    public static void setupPlayerTransforms(Object abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h)
    {
        // TODO: port to 1.21.11
    }

    public static void setupTransforms(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta)
    {
        // TODO: port to 1.21.11
    }

    public static void render(MatrixStack matrixStack, Entity entity, float tickDelta, Color color, Color lineColor, float lineWidth, boolean lines, boolean fill, boolean shine)
    {
        // TODO: port to 1.21.11
    }

    public static void renderHand(MatrixStack matrixStack, float tickDelta, Color lineColor, Color color,
                                  float lineWidth, boolean lines, boolean fill, boolean shine)
    {
        // TODO: port to 1.21.11
    }

    public static void renderFirstPersonItem(MatrixStack matrixStack, float tickDelta, PlayerEntityRenderer playerEntityRenderer, Arm arm, float swingProgress,
                                             float equipProgress, Color lineColor, Color color, float lineWidth, boolean lines, boolean fill, boolean shine)
    {
        // TODO: port to 1.21.11
    }

    public static void render(MatrixStack matrixStack, ModelPart part, Color color, Color lineColor, float lineWidth, boolean lines, boolean fill, boolean shine)
    {
        // TODO: port to 1.21.11
    }

    private static void renderModelPart(MatrixStack matrixStack, ModelPart.Cuboid cuboid, Color color, Color lineColor, float lineWidth, boolean lines, boolean fill, boolean shine)
    {
        // TODO: port to 1.21.11
    }

    public enum HandRenderType
    {
        RENDER_BOTH_HANDS(true, true),
        RENDER_MAIN_HAND_ONLY(true, false),
        RENDER_OFF_HAND_ONLY(false, true);

        public final boolean renderMainHand;
        public final boolean renderOffHand;

        HandRenderType(boolean renderMainHand, boolean renderOffHand)
        {
            this.renderMainHand = renderMainHand;
            this.renderOffHand = renderOffHand;
        }

        public static HandRenderType shouldOnlyRender(Hand hand)
        {
            return hand == Hand.MAIN_HAND ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
        }
    }
}
