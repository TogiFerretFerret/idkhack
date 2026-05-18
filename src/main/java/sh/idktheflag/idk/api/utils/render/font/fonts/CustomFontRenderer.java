package sh.idktheflag.idk.api.utils.render.font.fonts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import sh.idktheflag.idk.api.utils.math.HexRandom;
import sh.idktheflag.idk.api.utils.render.font.Glyph;
import sh.idktheflag.idk.api.utils.render.font.GlyphCache;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.features.modules.client.FontModule;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.io.Closeable;
import java.util.List;

public class CustomFontRenderer implements Closeable, IMinecraft
{
    private String font;
    private int size;
    private float scale = 2.0f;
    private int lastScale = -1;
    private final ObjectList<GlyphCache> caches = new ObjectArrayList<>();
    private final Char2ObjectArrayMap<Glyph> glyphs = new Char2ObjectArrayMap<>();
    private final Object2ObjectOpenHashMap<Identifier, ObjectList<CharLocation>> cache = new Object2ObjectOpenHashMap<>();

    public CustomFontRenderer(float size)
    {
        this.size = (int) size;
    }

    private void createFont(String fontName, int size)
    {
        this.font = fontName;
        this.size = size;
    }

    public void drawText(MatrixStack stack, String text, float x, float y, float r, float g, float b, float a, float brightnessMultiplier)
    {
    }

    public void drawText(MatrixStack stack, String text, double x, double y, Color color, boolean shadow) {
    }

    public float getStringWidth(String text) { return 0f; }
    public float getFontHeight() { return size; }
    @Override
    public void close() {}

    public record CharLocation(float x, float y, float r, float g, float b, Glyph glyph)
    {
    }
}
