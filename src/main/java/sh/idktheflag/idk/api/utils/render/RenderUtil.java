package sh.idktheflag.idk.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import sh.idktheflag.idk.api.gui.font.Fonts;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import sh.idktheflag.idk.api.utils.color.TextSection;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.utils.render.world.RenderType;
import sh.idktheflag.idk.api.utils.render.world.buffers.RenderBuffers;
import sh.idktheflag.idk.api.utils.render.world.layer.IdkLayers;
import sh.idktheflag.idk.impl.features.modules.client.FontModule;
import sh.idktheflag.idk.impl.features.modules.client.Optimizer;
import sh.idktheflag.idk.mixin.accessor.IWorldRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static sh.idktheflag.idk.api.utils.render.world.buffers.RenderBuffers.*;
import static sh.idktheflag.idk.api.wrapper.IMinecraft.mc;

public class RenderUtil {
    public static final Tessellator TESSELLATOR = Tessellator.getInstance();
    public static final Matrix4f matrix4f = new Matrix4f();

    public static void renderRect(MatrixStack matrices, double x1, double y1, double x2, double y2, int color)
    {
        renderRect(matrices, x1, y1, x2, y2, 0.0, color);
    }

    public static double interpolate(double oldValue, double newValue, double interpolationValue)
    {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static void renderItem(ItemStack stack, ItemDisplayContext renderMode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int seed)
    {
        // TODO: port to 1.21.11 - Item rendering API completely changed
    }

    public static VertexConsumer getItemGlintConsumer(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean glint)
    {
        if (glint)
        {
            return VertexConsumers.union(vertexConsumers.getBuffer(IdkLayers.ENCHANT), vertexConsumers.getBuffer(layer));
        }
        return vertexConsumers.getBuffer(layer);
    }


    public static void renderItemWithCount(DrawContext context, ItemStack item, Point pos, int count, Color textColor, boolean always)
    {
        context.drawItem(item, pos.x, pos.y);
        context.drawStackOverlay(mc.textRenderer, item, pos.x, pos.y);
        
        if ((count > 1) || always)
        {
            String text = String.valueOf(count);
            int xOffset = (count >= 1000) ? 19 : 17;
            Fonts.renderText(context, text, pos.x + xOffset - Fonts.getTextWidth(text), pos.y + 9, textColor, true);
        }
    }

    public static void renderOutline(MatrixStack matrices, double x1, double y1, double x2, double y2, int color, boolean rasturize)
    {
        renderOutlineRect(matrices, x1, y1, x2 - x1, y2 - y1, 0.0, color, rasturize);
    }

    public static void renderGradient(MatrixStack matrices, double startX, double startY, double endX, double endY, int colorStart, int colorEnd, boolean horizontal)
    {
        renderGradient(matrices, startX, startY, endX, endY, colorStart, colorEnd, horizontal, 0);
    }

    public static void renderGradient(MatrixStack matrices, double startX, double startY, double endX, double endY, int colorStart, int colorEnd, boolean horizontal, int z)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        renderGradient(matrices.peek().getPositionMatrix(), builder, startX, startY, endX, endY, z, colorStart, colorEnd, horizontal);
        BuiltBuffer built = builder.endNullable();
        if (built != null) {
            try {
                net.minecraft.client.render.RenderLayer.of("idk_gradient",
                    net.minecraft.client.render.RenderSetup.builder(net.minecraft.client.gl.RenderPipelines.DEBUG_QUADS).build()
                ).draw(built);
            } catch (Exception e) { try { built.close(); } catch (Exception ignored) {} }
        }
    }

    public static void renderGradient(Matrix4f matrix, BufferBuilder builder, double startX, double startY, double endX, double endY, double z, int colorStart, int colorEnd, boolean horizontal)
    {
        endX += startX;
        endY += startY;

        float f = (float) ColorHelper.getAlpha(colorStart) / 255.0f;
        float g = (float) ColorHelper.getRed(colorStart) / 255.0f;
        float h = (float) ColorHelper.getGreen(colorStart) / 255.0f;
        float i = (float) ColorHelper.getBlue(colorStart) / 255.0f;
        float j = (float) ColorHelper.getAlpha(colorEnd) / 255.0f;
        float k = (float) ColorHelper.getRed(colorEnd) / 255.0f;
        float l = (float) ColorHelper.getGreen(colorEnd) / 255.0f;
        float m = (float) ColorHelper.getBlue(colorEnd) / 255.0f;

        if (horizontal)
        {
            builder.vertex(matrix, (float) startX, (float) startY, (float) z).color(g, h, i, f);
            builder.vertex(matrix, (float) startX, (float) endY, (float) z).color(g, h, i, f);
            builder.vertex(matrix, (float) endX, (float) endY, (float) z).color(k, l, m, j);
            builder.vertex(matrix, (float) endX, (float) startY, (float) z).color(k, l, m, j);
        } else
        {
            builder.vertex(matrix, (float) startX, (float) startY, (float) z).color(g, h, i, f);
            builder.vertex(matrix, (float) startX, (float) endY, (float) z).color(k, l, m, j);
            builder.vertex(matrix, (float) endX, (float) endY, (float) z).color(k, l, m, j);
            builder.vertex(matrix, (float) endX, (float) startY, (float) z).color(g, h, i, f);
        }
    }

    public static void renderRect(MatrixStack matrices, double x1, double y1, double x2, double y2, double z, Color color)
    {
        renderRect(matrices, x1, y1, x2, y2, z, color.getRGB());
    }

    public static void renderRect(MatrixStack matrices, double x1, double y1, double x2, double y2, double z, int color)
    {
        x2 += x1;
        y2 += y1;
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        double i;
        if (x1 < x2)
        {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2)
        {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float f = (float) ColorHelper.getAlpha(color) / 255.0f;
        float g = (float) ColorHelper.getRed(color) / 255.0f;
        float h = (float) ColorHelper.getGreen(color) / 255.0f;
        float j = (float) ColorHelper.getBlue(color) / 255.0f;


        BufferBuilder buffer = TESSELLATOR.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, (float) x1, (float) y1, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, (float) x1, (float) y2, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, (float) x2, (float) y2, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, (float) x2, (float) y1, (float) z).color(g, h, j, f);

        BuiltBuffer built = buffer.endNullable();
        if (built != null) {
            try {
                net.minecraft.client.render.RenderLayer.of("idk_draw",
                    net.minecraft.client.render.RenderSetup.builder(net.minecraft.client.gl.RenderPipelines.DEBUG_QUADS).build()
                ).draw(built);
            } catch (Exception e) { try { built.close(); } catch (Exception ignored) {} }
        }
    }


    public static void drawText(String text, Vec3d renderPos, float size)
    {
        Camera camera = mc.gameRenderer.getCamera();
        final Vec3d pos = camera.getCameraPos();

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        matrixStack.translate(renderPos.x - pos.getX(), renderPos.y - pos.getY(), renderPos.z - pos.getZ());
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        matrixStack.scale(-0.01f * size, -0.01f * size, -1.0f);

        float distance = (float) renderPos.distanceTo(new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()));
        float scaleDistance = (distance / 2.0f) / (2.0f + (2.0f - size));
        if (scaleDistance < 1f)
            scaleDistance = 1;

        matrixStack.scale(scaleDistance, scaleDistance, scaleDistance);

        GL11.glDepthFunc(GL11.GL_ALWAYS);
        float hwidth = Fonts.getTextWidth(text) / 2.0f;
        Fonts.renderText(matrixStack, text, -hwidth, 0.0f, Color.WHITE, true);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        matrixStack.pop();
    }

    public static void renderOutlineRect(MatrixStack matrices, double x1, double y1, double width, double height, double z, Color color, boolean rasturize)
    {
        renderOutlineRect(matrices, x1, y1, width, height, z, color.getRGB(), rasturize);
    }

    public static void renderOutlineRect(MatrixStack matrices, double x1, double y1, double width, double height, double z, int color, boolean rasturize)
    {
        float x2 = (float) (x1 + width);
        float y2 = (float) (y1 + height);
        if (rasturize) y2 = y2 - 0.1f;
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float f = (float) ColorHelper.getAlpha(color) / 255.0f;
        float g = (float) ColorHelper.getRed(color) / 255.0f;
        float h = (float) ColorHelper.getGreen(color) / 255.0f;
        float j = (float) ColorHelper.getBlue(color) / 255.0f;
        
        BufferBuilder buffer = TESSELLATOR.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, (float) x1, (float) y1, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, (float) x1, (float) y2, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, (float) x1, y2, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, x2, y2, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, x2, y2, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, x2, (float) y1, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, (float) x2, (float) y1, (float) z).color(g, h, j, f);
        buffer.vertex(matrix4f, (float) x1, (float) y1, (float) z).color(g, h, j, f);
        
        BuiltBuffer built = buffer.endNullable();
        if (built != null) {
            try {
                net.minecraft.client.render.RenderLayer.of("idk_draw",
                    net.minecraft.client.render.RenderSetup.builder(net.minecraft.client.gl.RenderPipelines.DEBUG_QUADS).build()
                ).draw(built);
            } catch (Exception e) { try { built.close(); } catch (Exception ignored) {} }
        }
    }


    public static void renderBox(RenderType type, Box box, Color top, Color bottom)
    {
        switch (type)
        {
            case FILL:
                renderFillBox(box, top, bottom);
                break;
            case LINES:
                renderLinesBox(box, top, bottom);
                break;
        }
    }

    public static void renderLinesBox(Box box, Color top, Color bottom)
    {
        if (Optimizer.INSTANCE.isEnabled() && Optimizer.INSTANCE.frustrum.getValue() && !isFrustumVisible(box))
            return;

        MatrixStack stack = matrixFrom(box.minX, box.minY, box.minZ);
        stack.push();
        drawOutlineBox(stack, box.offset(new Vec3d(box.minX, box.minY, box.minZ).negate()), top, bottom);
        stack.pop();
    }

    public static boolean isFrustumVisible(Box box)
    {
        return true;
    }


    public static void renderFillBox(Box box, Color top, Color bottom)
    {
        if (Optimizer.INSTANCE.isEnabled() && Optimizer.INSTANCE.frustrum.getValue() && !isFrustumVisible(box))
        {
            return;
        }

        MatrixStack stack = matrixFrom(box.minX, box.minY, box.minZ);
        stack.push();
        drawBox(stack, 0, 0, 0, box.maxX - box.minX, box.maxY - box.minY, box.maxZ - box.minZ, top, bottom);
        stack.pop();
    }

    public static void drawBox(MatrixStack matrices, Box box, Color top, Color bottom)
    {
        drawBox(matrices, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, top, bottom);
    }

    public static void drawBox(double x1, double y1, double z1, double x2, double y2, double z2, Color color)
    {
        MatrixStack stack = matrixFrom(x1, y1, z1);
        stack.push();
        drawBox(stack, 0, 0, 0, x2 - x1, y2 - y1, z2 - z1, color, color);
        stack.pop();
    }


    public static MatrixStack matrixFrom(double x, double y, double z)
    {
        MatrixStack matrices = new MatrixStack();
        matrices.peek().getPositionMatrix().set(matrix4f);
        Vec3d pos = mc.gameRenderer.getCamera().getCameraPos();
        matrices.translate((float) (x - pos.x), (float) (y - pos.y), (float) (z - pos.z));
        return matrices;
    }

    public static void drawWorldLine(Vec3d a, Vec3d b, Color colorA, Color colorB)
    {
        MatrixStack matrices = matrixFrom(a.x, a.y, a.z);
        MatrixStack.Entry peek = matrices.peek();
        Matrix4f matrix4f = peek.getPositionMatrix();
        float dx = (float)(b.x - a.x), dy = (float)(b.y - a.y), dz = (float)(b.z - a.z);
        if (dx == 0 && dy == 0 && dz == 0) return;
        float len = MathHelper.sqrt(dx*dx + dy*dy + dz*dz);
        
        RenderBuffers.LINE.begin(matrix4f);
        RenderBuffers.LINE.buffer.vertex(matrix4f, 0, 0, 0)
                .color(colorA.getRed(), colorA.getGreen(), colorA.getBlue(), colorA.getAlpha())
                .normal(peek, dx/len, dy/len, dz/len)
                .lineWidth(1.0f);
        RenderBuffers.LINE.buffer.vertex(matrix4f, dx, dy, dz)
                .color(colorB.getRed(), colorB.getGreen(), colorB.getBlue(), colorB.getAlpha())
                .normal(peek, dx/len, dy/len, dz/len)
                .lineWidth(1.0f);
        RenderBuffers.LINE.end();
    }

    public static void drawBox(MatrixStack matrices, double x1, double y1,
                               double z1, double x2, double y2, double z2, Color top, Color bottom)
    {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        RenderBuffers.QUADS.begin(matrix4f);
        RenderBuffers.QUADS.color(bottom);
        RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x2, y1, z1).vertex(x2, y1, z2).vertex(x1, y1, z2);
        RenderBuffers.QUADS.color(top);
        RenderBuffers.QUADS.vertex(x1, y2, z1).vertex(x1, y2, z2).vertex(x2, y2, z2).vertex(x2, y2, z1);
        RenderBuffers.QUADS.color(bottom);
        RenderBuffers.QUADS.vertex(x1, y1, z1);
        RenderBuffers.QUADS.color(top);
        RenderBuffers.QUADS.vertex(x1, y2, z1).vertex(x2, y2, z1);
        RenderBuffers.QUADS.color(bottom);
        RenderBuffers.QUADS.vertex(x2, y1, z1);
        RenderBuffers.QUADS.vertex(x2, y1, z1);
        RenderBuffers.QUADS.color(top);
        RenderBuffers.QUADS.vertex(x2, y2, z1).vertex(x2, y2, z2);
        RenderBuffers.QUADS.color(bottom);
        RenderBuffers.QUADS.vertex(x2, y1, z2);
        RenderBuffers.QUADS.vertex(x1, y1, z2).vertex(x2, y1, z2);
        RenderBuffers.QUADS.color(top);
        RenderBuffers.QUADS.vertex(x2, y2, z2).vertex(x1, y2, z2);
        RenderBuffers.QUADS.color(bottom);
        RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x1, y1, z2);
        RenderBuffers.QUADS.color(top);
        RenderBuffers.QUADS.vertex(x1, y2, z2).vertex(x1, y2, z1);
        RenderBuffers.QUADS.end();
    }

    public static void renderTracerLine(Vec3d from, Vec3d to, Color top, Color bottom, float lineWidth)
    {
        drawWorldLine(from, to, top, bottom);
    }

    public static void renderLineFromPosToPos(Vec3d from, Vec3d to, Color top, Color bottom, float lineWidth)
    {
        renderLine(new Vec3d(0, 0, 0), from.x, from.y, from.z, to.x, to.y, to.z, top, bottom, lineWidth);
    }

    public static void renderLine(Vec3d offset, double x1, double y1, double z1, double x2, double y2, double z2, Color top, Color bottom, float width)
    {
        drawWorldLine(new Vec3d(x1, y1, z1).add(offset), new Vec3d(x2, y2, z2), top, bottom);
    }

    public static void drawOutlineBox(MatrixStack matrices, Box box, Color top, Color bottom)
    {
        renderLinesBox(matrices, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, top, bottom);
    }

    public static void drawCircle(Buffer buffer, float radius, int slices, Vec3d pos, Direction direction, Color color)
    {
        MatrixStack matrices = matrixFrom(pos.x, pos.y, pos.z);
        matrices.push();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.begin(matrix);
        buffer.color(color);
        for (int i = 0; i <= slices; i++)
        {
            double circleTwo = radius * Math.cos(i * (Math.PI * 4) / slices);
            double circleOne = radius * Math.sin(i * (Math.PI * 4) / slices);
            switch (direction)
            {
                case UP, DOWN:
                    buffer.vertex((float) circleTwo, 0f, (float) circleOne);
                    break;
                case EAST, WEST:
                    buffer.vertex(0f, (float) circleTwo, (float) circleOne);
                    break;
                case SOUTH, NORTH:
                    buffer.vertex((float) circleOne, (float) circleTwo, 0f);
                    break;
            }
        }
        buffer.end();
        matrices.pop();
    }

    public static void renderLinesBox(MatrixStack matrices, double x1, double y1,
                                      double z1, double x2, double y2, double z2, Color top, Color bottom)
    {
        Matrix4f m = matrices.peek().getPositionMatrix();
        MatrixStack.Entry peek = matrices.peek();
        drawEdge(peek, m, x1,y1,z1, x2,y1,z1, bottom);
        drawEdge(peek, m, x2,y1,z1, x2,y1,z2, bottom);
        drawEdge(peek, m, x2,y1,z2, x1,y1,z2, bottom);
        drawEdge(peek, m, x1,y1,z2, x1,y1,z1, bottom);
        drawEdge(peek, m, x1,y2,z1, x2,y2,z1, top);
        drawEdge(peek, m, x2,y2,z1, x2,y2,z2, top);
        drawEdge(peek, m, x2,y2,z2, x1,y2,z2, top);
        drawEdge(peek, m, x1,y2,z2, x1,y2,z1, top);
        drawEdge(peek, m, x1,y1,z1, x1,y2,z1, bottom, top);
        drawEdge(peek, m, x2,y1,z1, x2,y2,z1, bottom, top);
        drawEdge(peek, m, x2,y1,z2, x2,y2,z2, bottom, top);
        drawEdge(peek, m, x1,y1,z2, x1,y2,z2, bottom, top);
    }

    private static void drawEdge(MatrixStack.Entry peek, Matrix4f m,
                                  double x1, double y1, double z1,
                                  double x2, double y2, double z2, Color c)
    {
        drawEdge(peek, m, x1, y1, z1, x2, y2, z2, c, c);
    }

    private static void drawEdge(MatrixStack.Entry peek, Matrix4f m,
                                  double x1, double y1, double z1,
                                  double x2, double y2, double z2, Color c1, Color c2)
    {
        float dx = (float)(x2-x1), dy = (float)(y2-y1), dz = (float)(z2-z1);
        float len = MathHelper.sqrt(dx*dx + dy*dy + dz*dz);
        if (len == 0) return;
        RenderBuffers.LINE.begin(m);
        RenderBuffers.LINE.buffer.vertex(m, (float)x1, (float)y1, (float)z1)
                .color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha())
                .normal(peek, dx/len, dy/len, dz/len)
                .lineWidth(1.0f);
        RenderBuffers.LINE.buffer.vertex(m, (float)x2, (float)y2, (float)z2)
                .color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha())
                .normal(peek, dx/len, dy/len, dz/len)
                .lineWidth(1.0f);
        RenderBuffers.LINE.end();
    }

    public static Vector3d set(Vector3d vec, Entity entity, double tickDelta)
    {
        vec.x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        vec.y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        vec.z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
        return vec;
    }

    public static Vector3d set(Vector3d vec, Vec3d v)
    {
        vec.x = v.x;
        vec.y = v.y;
        vec.z = v.z;
        return vec;
    }

    public static void drawWaypoint(TextSection[] sections, double x, double y, double z, Camera camera, Color borderColor)
    {
        float width = 0;
        for (TextSection section : sections)
        {
            width += (int) Fonts.getTextWidth(section.getText());
        }
        width = width / 2;
        final Vec3d pos = camera.getCameraPos();
        MatrixStack matrices = new MatrixStack();
        final double maxRenderDistance = (mc.options.getViewDistance().getValue() << 4);
        Vec3d waypointVec = new Vec3d(x, y, z);
        Vec3d playerPos = Interpolator.getInterpolatedPosition(mc.getCameraEntity(), mc.getRenderTickCounter().getTickProgress(false));
        if (playerPos.distanceTo(waypointVec) > maxRenderDistance)
        {
            final Vec3d delta = waypointVec.subtract(playerPos).normalize();
            waypointVec = new Vec3d(delta.x * maxRenderDistance, delta.y * maxRenderDistance, delta.z * maxRenderDistance);
            waypointVec = playerPos.add(waypointVec);
        }
        x = waypointVec.x;
        y = waypointVec.y;
        z = waypointVec.z;
        Vec3d interpolate = Interpolator.getInterpolatedEyePos(mc.getCameraEntity(), mc.getRenderTickCounter().getTickProgress(false));
        double dx = (pos.getX() - interpolate.getX()) - x;
        double dy = (pos.getY() - interpolate.getY()) - y;
        double dz = (pos.getZ() - interpolate.getZ()) - z;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        float scaling = (float) (0.0018f + 0.003f * dist);
        if (dist <= 8.0) scaling = 0.0245f;
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        matrices.translate(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.scale(-scaling, -scaling, -1.0f);
        RenderUtil.renderOutline(matrices, -width - 1.0f, -1.0f, width * 2.0f + 2.0f, mc.textRenderer.fontHeight + 1.5f, borderColor.getRGB(), true);
        drawSections(sections, matrices, -width, 0.0f);
        matrices.pop();
    }

    public static void drawSections(TextSection[] sections, MatrixStack matrices, float x, float y)
    {
        float width = 0;
        for (TextSection section : sections)
        {
            Fonts.renderText(matrices, section.getText(), x + width, y, section.getColor(), FontModule.INSTANCE.textShadow.getValue());
            width += Fonts.getTextWidth(section.getText());
        }
    }
}
