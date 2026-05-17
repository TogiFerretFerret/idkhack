package me.skitttyy.kami.api.utils.render;

import me.skitttyy.kami.api.event.events.render.RenderWorldEvent;
import me.skitttyy.kami.api.utils.render.world.RenderType;
import me.skitttyy.kami.api.wrapper.IMinecraft;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import java.awt.*;

import static me.skitttyy.kami.api.utils.render.world.buffers.RenderBuffers.LINES;
import static me.skitttyy.kami.api.utils.render.world.buffers.RenderBuffers.QUADS;


public class WireframeEntityRenderer implements IMinecraft
{
    private static MatrixStack matrices = new MatrixStack();
    private static final Vector4f pos1 = new Vector4f();
    private static final Vector4f pos2 = new Vector4f();
    private static final Vector4f pos3 = new Vector4f();
    private static final Vector4f pos4 = new Vector4f();

    //    private static double offsetX, offsetY, offsetZ;
    private static Color sideColor, lineColor;
    private static RenderType shapeMode;

    private WireframeEntityRenderer()
    {
    }

//    public static void render(RenderWorldEvent event, Entity entity, double scale, java.awt.Color sideColor, Color lineColor, RenderType type)
//    {
//        WireframeEntityRenderer.sideColor = sideColor;
//        WireframeEntityRenderer.lineColor = lineColor;
//        WireframeEntityRenderer.shapeMode = shapeMode;
//
//        offsetX = MathHelper.lerp(event.getTickProgress(), entity.lastRenderX, entity.getX());
//        offsetY = MathHelper.lerp(event.getTickProgress(), entity.lastRenderY, entity.getY());
//        offsetZ = MathHelper.lerp(event.getTickProgress(), entity.lastRenderZ, entity.getZ());
//
//        matrices.push();
//        matrices.scale((float) scale, (float) scale, (float) scale);
//
//        EntityRenderer<?> entityRenderer = mc.getEntityRenderDispatcher().getRenderer(entity);
//
//        // LivingEntityRenderer
//        if (entityRenderer instanceof LivingEntityRenderer renderer)
//        {
//            LivingEntity livingEntity = (LivingEntity) entity;
//            EntityModel<LivingEntity> model = renderer.getModel();
//
//            // PlayerEntityRenderer
//            if (entityRenderer instanceof PlayerEntityRenderer r)
//            {
//                PlayerEntityModel<AbstractClientPlayerEntity> playerModel = r.getModel();
//
//                playerModel.sneaking = entity.isInSneakingPose();
//                BipedEntityModel.ArmPose armPose = PlayerEntityRenderer.getArmPose((AbstractClientPlayerEntity) entity, Hand.MAIN_HAND);
//                BipedEntityModel.ArmPose armPose2 = PlayerEntityRenderer.getArmPose((AbstractClientPlayerEntity) entity, Hand.OFF_HAND);
//
//                if (armPose.isTwoHanded())
//                    armPose2 = livingEntity.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
//
//                if (livingEntity.getMainArm() == Arm.RIGHT)
//                {
//                    playerModel.rightArmPose = armPose;
//                    playerModel.leftArmPose = armPose2;
//                } else
//                {
//                    playerModel.rightArmPose = armPose2;
//                    playerModel.leftArmPose = armPose;
//                }
//            }
//
//            model.handSwingProgress = livingEntity.getHandSwingProgress(event.getTickProgress());
//            model.riding = livingEntity.hasVehicle();
//            model.child = livingEntity.isBaby();
//
//            float bodyYaw = MathHelper.lerpAngleDegrees(event.getTickProgress(), livingEntity.prevBodyYaw, livingEntity.bodyYaw);
//            float headYaw = MathHelper.lerpAngleDegrees(event.getTickProgress(), livingEntity.lastHeadYaw, livingEntity.headYaw);
//            float yaw = headYaw - bodyYaw;
//
//            float animationProgress;
//            if (livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof LivingEntity livingEntity2)
//            {
//                bodyYaw = MathHelper.lerpAngleDegrees(event.getTickProgress(), livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
//                yaw = headYaw - bodyYaw;
//                animationProgress = MathHelper.wrapDegrees(yaw);
//
//                if (animationProgress < -85) animationProgress = -85;
//                if (animationProgress >= 85) animationProgress = 85;
//
//                bodyYaw = headYaw - animationProgress;
//                if (animationProgress * animationProgress > 2500) bodyYaw += animationProgress * 0.2;
//
//                yaw = headYaw - bodyYaw;
//            }
//
//            float pitch = MathHelper.lerp(event.getTickProgress(), livingEntity.lastPitch, livingEntity.getPitch());
//
//            animationProgress = renderer.getAnimationProgress(livingEntity, event.getTickProgress());
//            float limbDistance = 0;
//            float limbAngle = 0;
//
//            if (!livingEntity.hasVehicle() && livingEntity.isAlive())
//            {
//                limbDistance = livingEntity.limbAnimator.getSpeed(event.getTickProgress());
//                limbAngle = livingEntity.limbAnimator.getPos(event.getTickProgress());
//
//                if (livingEntity.isBaby()) limbAngle *= 3;
//                if (limbDistance > 1) limbDistance = 1;
//            }
//
//            model.animateModel(livingEntity, limbAngle, limbDistance, event.getTickProgress());
//            model.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, yaw, pitch);
//
//            renderer.setupTransforms(livingEntity, matrices, animationProgress, bodyYaw, event.getTickProgress(), livingEntity.getScale());
//            matrices.scale(-1, -1, 1);
//            renderer.scale(livingEntity, matrices, event.getTickProgress());
//            matrices.translate(0, -1.5010000467300415, 0);
//
//            // Render
//
//        }
//
//        if (entityRenderer instanceof EndCrystalEntityRenderer renderer)
//        {
//            EndCrystalEntity crystalEntity = (EndCrystalEntity) entity;
//            boolean chamsEnabled = false;
//
//            matrices.push();
//            float h = EndCrystalEntityRenderer.getYOffset(crystalEntity, event.getTickProgress());
//            float j = ((float) crystalEntity.endCrystalAge + event.getTickProgress()) * 3.0F;
//            matrices.push();
//            matrices.scale(2.0F, 2.0F, 2.0F);
//            matrices.translate(0.0D, -0.5D, 0.0D);
//            if (crystalEntity.shouldShowBottom()) render(renderer.bottom);
//
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
//            matrices.translate(0.0D, 1.5F + h / 2.0F, 0.0D);
//            matrices.multiply(new Quaternionf().setAngleAxis(60.0F, EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES));
//            render(renderer.frame);
//            matrices.scale(0.875F, 0.875F, 0.875F);
//            matrices.multiply(new Quaternionf().setAngleAxis(60.0F, EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES));
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
//            render(renderer.frame);
//            matrices.scale(0.875F, 0.875F, 0.875F);
//            matrices.multiply(new Quaternionf().setAngleAxis(60.0F, EndCrystalEntityRenderer.SINE_45_DEGREES, 0.0F, EndCrystalEntityRenderer.SINE_45_DEGREES));
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
//            render(renderer.core);
//            matrices.pop();
//            matrices.pop();
//        } else if (entityRenderer instanceof BoatEntityRenderer renderer)
//        {
//            BoatEntity boatEntity = (BoatEntity) entity;
//
//            matrices.push();
//            matrices.translate(0.0D, 0.375D, 0.0D);
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - MathHelper.lerp(event.getTickProgress(), entity.lastYaw, entity.getYaw())));
//            float h = (float) boatEntity.getDamageWobbleTicks() - event.getTickProgress();
//            float j = boatEntity.getDamageWobbleStrength() - event.getTickProgress();
//            if (j < 0.0F) j = 0.0F;
//
//            if (h > 0.0F)
//            {
//                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(h) * h * j / 10.0F * (float) boatEntity.getDamageWobbleSide()));
//            }
//
//            float k = boatEntity.interpolateBubbleWobble(event.getTickProgress());
//            if (!MathHelper.approximatelyEquals(k, 0.0F))
//            {
//                matrices.multiply(new Quaternionf().setAngleAxis(boatEntity.interpolateBubbleWobble(event.getTickProgress()), 1.0F, 0.0F, 1.0F));
//            }
//
//            CompositeEntityModel<BoatEntity> boatEntityModel = renderer.texturesAndModels.get(boatEntity.getVariant()).getSecond();
//            matrices.scale(-1.0F, -1.0F, 1.0F);
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
//            boatEntityModel.setAngles(boatEntity, event.getTickProgress(), 0.0F, -0.1F, 0.0F, 0.0F);
//            boatEntityModel.getParts().forEach(modelPart -> render(modelPart));
//            if (!boatEntity.isSubmergedInWater() && boatEntityModel instanceof ModelWithWaterPatch modelWithWaterPatch)
//                render(modelWithWaterPatch.getWaterPatch());
//
//            matrices.pop();
//        } else if (entityRenderer instanceof ItemEntityRenderer)
//        {
//            double dx = (entity.getX() - entity.lastX) * event.getTickProgress();
//            double dy = (entity.getY() - entity.lastY) * event.getTickProgress();
//            double dz = (entity.getZ() - entity.lastZ) * event.getTickProgress();
//
//            Box box = entity.getBoundingBox();
//            RenderUtil.renderLinesBox(matrices, dx + box.minX, dy + box.minY, dz + box.minZ, dx + box.maxX, dy + box.maxY, dz + box.maxZ, lineColor, lineColor);
//        }
//
//        matrices.pop();
//    }

    private static void render(ModelPart part)
    {
        if (!part.visible || (part.cuboids.isEmpty() && part.children.isEmpty())) return;

        matrices.push();
        // TODO: port to 1.21.11 - ModelPart.rotate() no longer takes MatrixStack, takes Quaternionf or Vector3f
        // part.rotate(matrices);

//        for (ModelPart.Cuboid cuboid : part.cuboids) render( cuboid, offsetX, offsetY, offsetZ);
        for (ModelPart.Cuboid cuboid : part.cuboids) render(cuboid);
        for (ModelPart child : part.children.values()) render(child);

        matrices.pop();
    }

    private static void render(ModelPart.Cuboid cuboid)
    {
        for (ModelPart.Quad quad : cuboid.sides)
        {
            // Transform positions - In 1.21.11, vertices is accessed via vertices() and pos fields via x()/y()/z()
            ModelPart.Vertex[] verts = quad.vertices();
            pos1.set(verts[0].x() / 16, verts[0].y() / 16, verts[0].z() / 16, 1);

            pos2.set(verts[1].x() / 16, verts[1].y() / 16, verts[1].z() / 16, 1);

            pos3.set(verts[2].x() / 16, verts[2].y() / 16, verts[2].z() / 16, 1);

            pos4.set(verts[3].x() / 16, verts[3].y() / 16, verts[3].z() / 16, 1);

            // Render
            if (shapeMode.sides())
            {

                Matrix4f matrix4f = matrices.peek().getPositionMatrix();

                QUADS.begin(matrix4f);
                QUADS.color(sideColor);
                QUADS.vertex(pos1.x, pos1.y, pos1.z);
                QUADS.vertex(pos2.x, pos2.y, pos2.z);
                QUADS.vertex(pos3.x, pos3.y, pos3.z);
                QUADS.vertex(pos4.x, pos4.y, pos4.z);
                QUADS.end();
//                renderer.triangles.quad(
//
//                        renderer.triangles.vec3(offsetX + pos1.x, offsetY + pos1.y, offsetZ + pos1.z).color(sideColor).next(),
//                        renderer.triangles.vec3(offsetX + pos2.x, offsetY + pos2.y, offsetZ + pos2.z).color(sideColor).next(),
//                        renderer.triangles.vec3(offsetX + pos3.x, offsetY + pos3.y, offsetZ + pos3.z).color(sideColor).next(),
//                        renderer.triangles.vec3(offsetX + pos4.x, offsetY + pos4.y, offsetZ + pos4.z).color(sideColor).next()
//                );
            }

            if (shapeMode.lines())
            {
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                LINES.begin(matrix4f);
                LINES.color(lineColor);
                LINES.vertex(pos1.x, pos1.y, pos1.z);
                LINES.vertex(pos2.x, pos2.y, pos2.z);

                LINES.vertex(pos2.x, pos2.y, pos2.z);
                LINES.vertex(pos3.x, pos3.y, pos3.z);

                LINES.vertex(pos3.x, pos3.y, pos3.z);
                LINES.vertex(pos4.x, pos4.y, pos4.z);


                LINES.vertex(pos1.x, pos1.y, pos1.z);
                LINES.vertex(pos1.x, pos1.y, pos1.z);

                LINES.end();

//                renderer.line(offsetX + pos1.x, offsetY + pos1.y, offsetZ + pos1.z, offsetX + pos2.x, offsetY + pos2.y, offsetZ + pos2.z, lineColor);
//                renderer.line(offsetX + pos2.x, offsetY + pos2.y, offsetZ + pos2.z, offsetX + pos3.x, offsetY + pos3.y, offsetZ + pos3.z, lineColor);
//                renderer.line(offsetX + pos3.x, offsetY + pos3.y, offsetZ + pos3.z, offsetX + pos4.x, offsetY + pos4.y, offsetZ + pos4.z, lineColor);
//                renderer.line(offsetX + pos1.x, offsetY + pos1.y, offsetZ + pos1.z, offsetX + pos1.x, offsetY + pos1.y, offsetZ + pos1.z, lineColor);
            }
        }
    }

    // TODO: port to 1.21.11 - EntityModel no longer generic over LivingEntity (now uses EntityRenderState),
    // AnimalModel fields (child, headScaled, etc) removed, SinglePartEntityModel/CompositeEntityModel removed,
    // LlamaEntityModel/RabbitEntityModel fields no longer accessible. Needs complete rewrite.
    public static void renderModel(MatrixStack matrix, EntityModel<?> model, RenderType type, Color sideColor, Color lineColor)
    {
        matrices = matrix;
        shapeMode = type;

        WireframeEntityRenderer.sideColor = sideColor;
        WireframeEntityRenderer.lineColor = lineColor;

        // Render all parts of the model
        for (ModelPart part : model.getParts()) {
            render(part);
        }
    }


    public static void renderPart(MatrixStack matrix, ModelPart part, RenderType type, Color sideColor, Color lineColor)
    {
        matrices = matrix;
        shapeMode = type;


        WireframeEntityRenderer.sideColor = sideColor;
        WireframeEntityRenderer.lineColor = lineColor;

        matrix.push();
        render(part);
        matrix.pop();
    }

}