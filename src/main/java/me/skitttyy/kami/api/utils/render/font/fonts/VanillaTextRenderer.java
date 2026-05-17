package me.skitttyy.kami.api.utils.render.font.fonts;

import me.skitttyy.kami.api.wrapper.IMinecraft;
import me.skitttyy.kami.impl.features.modules.client.FontModule;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

/**
 * @author Shoreline
 * pasted by Skitttyy
 */
public class VanillaTextRenderer implements IMinecraft {


    public void renderTextNoLayer(DrawContext context, MatrixStack matrices, String text, float x, float y, int color, boolean shadow)
    {

        if (shadow)
            drawNoLayer(context, text, x + (FontModule.INSTANCE.shortShadow.getValue() ? 0.3f : 1.0f), y + (FontModule.INSTANCE.shortShadow.getValue() ? 0.3f : 1.0f), color, matrices.peek().getPositionMatrix(), true);
        drawNoLayer(context, text, x, y, color, matrices.peek().getPositionMatrix(), false);
    }



    public void renderCsgoLayer(DrawContext context, MatrixStack matrices, String text, float x, float y, int color)
    {
        drawNoLayer(context, text, x + 0.4f, y + 0.4f, color, matrices.peek().getPositionMatrix(), true);
        drawNoLayer(context, text, x - 0.4f, y - 0.4f, color, matrices.peek().getPositionMatrix(), true);
        drawNoLayer(context, text, x - 0.4f, y + 0.4f, color, matrices.peek().getPositionMatrix(), true);
        drawNoLayer(context, text, x + 0.4f, y - 0.4f, color, matrices.peek().getPositionMatrix(), true);

        drawNoLayer(context, text, x, y, color, matrices.peek().getPositionMatrix(), false);
    }


    private void drawNoLayer(DrawContext context, String text, float x, float y, int color, Matrix4f matrix, boolean shadow)
    {
        if (text == null)
        {
            return;
        }
        VertexConsumerProvider.Immediate vertexConsumers = mc.getBufferBuilders().getEntityVertexConsumers();
        mc.textRenderer.draw(text, x, y, color, shadow, matrix,
                vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
    }

    public void drawText(DrawContext context, MatrixStack matrices, String text, float x, float y, int color, boolean shadow)
    {
        if (shadow)
            draw(context, matrices, text, x + (FontModule.INSTANCE.shortShadow.getValue() ? 0.3f : 1.0f), y + (FontModule.INSTANCE.shortShadow.getValue() ? 0.3f : 1.0f), color, true);
        draw(context, matrices, text, x, y, color, false);
    }

    public void draw(DrawContext context, MatrixStack matrices, String text, float x, float y, int color, boolean shadow)
    {
        this.draw(context, text, x, y, color, matrices.peek().getPositionMatrix(), shadow);
    }

    private void draw(DrawContext context, String text, float x, float y, int color, Matrix4f matrix, boolean shadow)
    {
        if (text == null)
        {
            return;
        }
        VertexConsumerProvider.Immediate consumers = mc.getBufferBuilders().getEntityVertexConsumers();
        mc.textRenderer.draw(text, x, y, color, shadow, matrix,
                consumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        consumers.draw();
    }

    public void drawText(MatrixStack matrices, String text, float x, float y, int color, boolean shadow)
    {
        if (shadow)
            draw(matrices, text, x + (FontModule.INSTANCE.shortShadow.getValue() ? 0.3f : 1.0f), y + (FontModule.INSTANCE.shortShadow.getValue() ? 0.3f : 1.0f), color, true);
        draw(matrices, text, x, y, color, false);
    }

    public void draw(MatrixStack matrices, String text, float x, float y, int color, boolean shadow)
    {
        this.draw(text, x, y, color, matrices.peek().getPositionMatrix(), shadow);
    }

    private void draw(String text, float x, float y, int color, Matrix4f matrix, boolean shadow)
    {
        if (text == null)
        {
            return;
        }

        VertexConsumerProvider.Immediate vertexConsumers = mc.getBufferBuilders().getEntityVertexConsumers();
        mc.textRenderer.draw(text, x, y, color, shadow, matrix,
                vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
        vertexConsumers.draw();
    }

    // TODO: port to 1.21.11 - The custom Drawer class used GlyphRenderer, FontStorage.getGlyph(),
    // FontStorage.getGlyphRenderer(), EmptyGlyphRenderer, Glyph.getBoldOffset(), Glyph.getAdvance(),
    // GlyphRenderer.getLayer(), GlyphRenderer.drawRectangle(), ITextRenderer.hookDrawGlyph() etc.
    // All of these APIs have been removed/replaced in 1.21.11's new font rendering system.
    // Using mc.textRenderer.draw() as a fallback for now.
}
