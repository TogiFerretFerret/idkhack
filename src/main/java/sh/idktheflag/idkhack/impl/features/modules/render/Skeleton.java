package sh.idktheflag.idkhack.impl.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import sh.idktheflag.idkhack.api.event.eventbus.Priority;
import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.RotationManager;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.utils.ducks.ILivingEntity;
import sh.idktheflag.idkhack.api.utils.render.Interpolator;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.features.modules.client.AntiCheat;
import sh.idktheflag.idkhack.impl.features.modules.movement.ElytraFly;
import sh.idktheflag.idkhack.impl.features.modules.movement.Flight;
import sh.idktheflag.idkhack.impl.features.modules.movement.LongJump;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import static sh.idktheflag.idkhack.api.utils.render.world.buffers.RenderBuffers.LINES;

public class Skeleton extends Module
{

    public Value<Sn0wColor> color = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Color")
            .withValue(new Sn0wColor(255, 255, 255))
            .register(this);

    public static Skeleton INSTANCE;

    public Skeleton()
    {
        super("Skeleton", Category.Render);
        INSTANCE = this;
    }

    @SubscribeEvent(Priority.MODULE_LAST)
    public void onRenderWorld(RenderWorldEvent event)
    {


        if (mc.gameRenderer == null || mc.getCameraEntity() == null)
        {
            return;
        }
        // TODO: port to 1.21.11 - float g = event.getTickProgress();
        // TODO: port to 1.21.11 - RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        // RenderSystem.enableBlend(); // TODO: port to 1.21.11
        // RenderSystem.defaultBlendFunc(); // TODO: port to 1.21.11
        // RenderSystem.disableDepthTest(); // TODO: port to 1.21.11
        // TODO: port to 1.21.11 - RenderSystem.depthMask(mc.isFancyGraphicsOrBetter());
        // RenderSystem.enableCull(); // TODO: port to 1.21.11
        for (Entity entity : mc.world.getEntities())
        {
            if (entity == null || !entity.isAlive())
            {
                continue;
            }
            if (entity instanceof PlayerEntity playerEntity)
            {
                if (mc.options.getPerspective().isFirstPerson() && playerEntity == mc.player)
                {
                    continue;
                }


                // TODO 1.21.11: Vec3d skeletonPos = Interpolator.getInterpolatedPosition(entity, g);

                // TODO: port to 1.21.11 - EntityRenderDispatcher.getRenderer changed
                PlayerEntityRenderer livingEntityRenderer = null;
                PlayerEntityModel playerModel = null;

                // TODO: port to 1.21.11 - prevBodyYaw/bodyYaw/lastHeadYaw/headYaw removed
                float h = 0; // was MathHelper.lerpAngleDegrees(g, playerEntity.prevBodyYaw, playerEntity.bodyYaw)
                float j = 0; // was MathHelper.lerpAngleDegrees(g, playerEntity.lastHeadYaw, playerEntity.headYaw)


                // TODO: port to 1.21.11 - Skeleton rendering needs full rewrite for new model/render state system
                BipedEntityModel.ArmPose armPose = BipedEntityModel.ArmPose.EMPTY;
                BipedEntityModel.ArmPose armPose2 = BipedEntityModel.ArmPose.EMPTY;

                if (playerEntity.getMainArm() == Arm.RIGHT)
                {
                    // TODO 1.21.11: playerModel.rightArmPose = armPose;
                    // TODO 1.21.11: playerModel.leftArmPose = armPose2;
                } else
                {
                    // TODO 1.21.11: playerModel.rightArmPose = armPose2;
                    // TODO 1.21.11: playerModel.leftArmPose = armPose;
                }


                float n = 0.0f;
                float o = 0.0f;
                if (!playerEntity.hasVehicle() && playerEntity.isAlive())
                {
                    // TODO: port to 1.21.11 - n = playerEntity.limbAnimator.getSpeed(event.getTickProgress());
                    // TODO 1.21.11: o = playerEntity.limbAnimator.getPos(event.getTickProgress());
                    if (playerEntity.isBaby())
                    {
                        o *= 3.0f;
                    }
                    if (n > 1.0f)
                    {
                        n = 1.0f;
                    }
                }
                // TODO 1.21.11: float l = playerEntity.age + event.getTickProgress();
                float k = j - h;
                // TODO 1.21.11: float m = playerEntity.getPitch(g);


                if (AntiCheat.INSTANCE.visualize.getValue() && playerEntity == mc.player && !RotationManager.INSTANCE.FROM_INV)
                {
                    ILivingEntity accessor = (ILivingEntity) playerEntity;
                    // TODO 1.21.11: m = MathHelper.lerpAngleDegrees(g, accessor.kami_getPrevHeadPitch(), accessor.kami_getHeadPitch());
                }



                // TODO 1.21.11: playerModel.animateModel((AbstractClientPlayerEntity) playerEntity, o, n, event.getTickProgress());
                // TODO 1.21.11: playerModel.setAngles((AbstractClientPlayerEntity) playerEntity, o, n, l, k, m);


                boolean swimming = playerEntity.isInSwimmingPose();
                boolean sneaking = playerEntity.isInSneakingPose();
                boolean flying = playerEntity.isGliding();

                if (entity == mc.player && (LongJump.isGrimJumping() || Flight.isGrimFlying() || ElytraFly.isPacketFlying()))
                    flying = false;
                ModelPart head = playerModel.head;
                ModelPart leftArm = playerModel.leftArm;
                ModelPart rightArm = playerModel.rightArm;
                ModelPart leftLeg = playerModel.leftLeg;
                ModelPart rightLeg = playerModel.rightLeg;


                // TODO 1.21.11: playerModel.sneaking = entity.isInSneakingPose();


                MatrixStack matrixStack = RenderUtil.matrixFrom(skeletonPos.x, skeletonPos.y, skeletonPos.z);
                matrixStack.push();
                if (swimming)
                {
                    matrixStack.translate(0, 0.35f, 0);
                }
                matrixStack.multiply(new Quaternionf().setAngleAxis((h + 180.0f) * Math.PI / 180.0f, 0, -1, 0));
                if (swimming || flying)
                {
                    matrixStack.multiply(new Quaternionf().setAngleAxis((90.0f + m) * Math.PI / 180.0f, -1, 0, 0));
                }
                if (swimming)
                {
                    matrixStack.translate(0, -0.95f, 0);
                }

                Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
                LINES.begin(matrix4f);
                LINES.color(color.getValue().getColor());
                LINES.vertex(0, sneaking ? 0.6f : 0.7f,
                        sneaking ? 0.23f : 0);
                LINES.vertex(0, sneaking ? 1.05f : 1.4f,
                        0);
                LINES.vertex(-0.37f, sneaking ? 1.05f :
                        1.35f, 0);
                LINES.vertex(0.37f, sneaking ? 1.05f :
                        1.35f, 0);
                LINES.vertex(-0.15f, sneaking ? 0.6f :
                        0.7f, sneaking ? 0.23f : 0);
                LINES.vertex(0.15f, sneaking ? 0.6f : 0.7f,
                        sneaking ? 0.23f : 0);
                matrixStack.push();
                matrixStack.translate(0, sneaking ? 1.05f : 1.4f, 0);
                // TODO 1.21.11: rotateSkeleton(matrixStack, head);
                matrix4f = matrixStack.peek().getPositionMatrix();
                LINES.vertex(matrix4f, 0, 0, 0);
                LINES.vertex(matrix4f, 0, 0.25f, 0);
                matrixStack.pop();
                matrixStack.push();
                matrixStack.translate(0.15f, sneaking ? 0.6f : 0.7f, sneaking ? 0.23f : 0);
                // TODO 1.21.11: rotateSkeleton(matrixStack, rightLeg);
                matrix4f = matrixStack.peek().getPositionMatrix();
                LINES.vertex(matrix4f, 0, 0, 0);
                LINES.vertex(matrix4f, 0, -0.6f, 0);
                matrixStack.pop();
                matrixStack.push();
                matrixStack.translate(-0.15f, sneaking ? 0.6f : 0.7f, sneaking ? 0.23f : 0);
                // TODO 1.21.11: rotateSkeleton(matrixStack, leftLeg);
                matrix4f = matrixStack.peek().getPositionMatrix();
                LINES.vertex(matrix4f, 0, 0, 0);
                LINES.vertex(matrix4f, 0, -0.6f, 0);
                matrixStack.pop();
                matrixStack.push();
                matrixStack.translate(0.37f, sneaking ? 1.05f : 1.35f, 0);
                // TODO 1.21.11: rotateSkeleton(matrixStack, rightArm);
                matrix4f = matrixStack.peek().getPositionMatrix();
                LINES.vertex(matrix4f, 0, 0, 0);
                LINES.vertex(matrix4f, 0, -0.55f, 0);
                matrixStack.pop();
                matrixStack.push();
                matrixStack.translate(-0.37f, sneaking ? 1.05f : 1.35f, 0);
                // TODO 1.21.11: rotateSkeleton(matrixStack, leftArm);
                matrix4f = matrixStack.peek().getPositionMatrix();
                LINES.vertex(matrix4f, 0, 0, 0);
                LINES.vertex(matrix4f, 0, -0.55f, 0);
                matrixStack.pop();
                LINES.draw();
                if (swimming)
                {
                    matrixStack.translate(0, 0.95f, 0);
                }
                if (swimming || flying)
                {
                    matrixStack.multiply(new Quaternionf().setAngleAxis((90.0f + m) * Math.PI / 180.0f, 1, 0, 0));
                }
                if (swimming)
                {
                    matrixStack.translate(0, -0.35f, 0);
                }
                matrixStack.multiply(new Quaternionf().setAngleAxis((h + 180.0f) * Math.PI / 180.0f, 0, 1, 0));
                matrixStack.translate(-skeletonPos.x, -skeletonPos.y, -skeletonPos.z);
                matrixStack.pop();
            }
        }
        // RenderSystem.disableCull(); // TODO: port to 1.21.11
        // RenderSystem.disableBlend(); // TODO: port to 1.21.11
        // RenderSystem.enableDepthTest(); // TODO: port to 1.21.11
        // TODO: 1.21.11 - RenderSystem.depthMask(true);
        // TODO: 1.21.11 - RenderSystem.setShader(GameRenderer::getPositionColorProgram);
    }

    // TODO: 1.21.11 - private void rotateSkeleton(MatrixStack matrix, ModelPart modelPart)
    {
        if (false) // TODO 1.21.11: if (modelPart.roll != 0.0f)
        {
            // TODO 1.21.11: matrix.multiply(RotationAxis.POSITIVE_Z.rotation(modelPart.roll));
        }
        if (false) // TODO 1.21.11: if (modelPart.yaw != 0.0f)
        {
            // TODO 1.21.11: matrix.multiply(RotationAxis.NEGATIVE_Y.rotation(modelPart.yaw));
        }
        if (false) // TODO 1.21.11: if (modelPart.pitch != 0.0f)
        {
            // TODO 1.21.11: matrix.multiply(RotationAxis.NEGATIVE_X.rotation(modelPart.pitch));
        }
    }

    @Override
    public String getDescription()
    {
        return "Skeleton: renders players skeletons";
    }
}