package sh.idktheflag.idkhack.api.utils.render.world.buffers;

import com.mojang.blaze3d.vertex.VertexFormat;
import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class RenderBuffers {
    public static final Buffer QUADS = new Buffer(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
    public static final Buffer LINES = new Buffer(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
    public static final Buffer LINE = new Buffer(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL_LINE_WIDTH);
    public static final Buffer TRIANGLES = new Buffer(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

    private static boolean isSetup = false;
    private static final List<Runnable> processes = new ArrayList<>();
    private static final List<Runnable> before = new ArrayList<>();
    private static final List<Runnable> midways = new ArrayList<>();

    // RenderLayers for each draw mode, cached
    private static final RenderLayer QUADS_LAYER = RenderLayer.of("idkhack_quads",
            RenderSetup.builder(RenderPipelines.DEBUG_QUADS).build());
    private static final RenderLayer LINES_LAYER = RenderLayer.of("idkhack_lines",
            RenderSetup.builder(RenderPipelines.LINES).build());
    private static final RenderLayer DEBUG_LINES_LAYER = RenderLayer.of("idkhack_debug_lines",
            RenderSetup.builder(RenderPipelines.DEBUG_QUADS).build());
    private static final RenderLayer TRIANGLES_LAYER = RenderLayer.of("idkhack_triangles",
            RenderSetup.builder(RenderPipelines.DEBUG_TRIANGLE_FAN).build());

    public static void preRender()
    {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        isSetup = true;

        for (Runnable callback : midways)
        {
            callback.run();
        }
        midways.clear();
    }

    public static void process()
    {
        for (Runnable callback : before)
        {
            callback.run();
        }
        before.clear();

        for (Runnable callback : processes)
        {
            callback.run();
        }
        processes.clear();
    }

    public static void postRender()
    {
        QUADS.draw();
        TRIANGLES.draw();
        LINES.draw();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        LINE.draw();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        isSetup = false;
    }

    public static void scheduleMidway(Runnable callback)
    {
        midways.add(callback);
    }

    public static void scheduleRender(Runnable callback)
    {
        processes.add(callback);
    }

    public static void schedulePreRender(Runnable callback)
    {
        before.add(callback);
    }


    public static class Buffer {
        public BufferBuilder buffer;
        private Matrix4f positionMatrix;
        VertexFormat.DrawMode drawMode;
        VertexFormat vertexFormat;
        Color currentColor;

        public Buffer(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat)
        {
            this.drawMode = drawMode;
            this.vertexFormat = vertexFormat;
        }

        public void begin(Matrix4f positionMatrix)
        {
            this.positionMatrix = positionMatrix;
            this.buffer = Tessellator.getInstance().begin(drawMode, vertexFormat);
        }
        public void begin(MatrixStack positionMatrix)
        {
            this.positionMatrix = positionMatrix.peek().getPositionMatrix();
            this.buffer = Tessellator.getInstance().begin(drawMode, vertexFormat);
        }

        public void end()
        {
            draw();
        }

        public Buffer vertex(double x, double y, double z)
        {
            return vertex((float) x, (float) y, (float) z);
        }

        public Buffer vertex(float x, float y, float z)
        {
            if (currentColor == null) currentColor = Color.WHITE;
            ColorUtil.color(buffer.vertex(positionMatrix, x, y, z), currentColor);
            return this;
        }

        public Buffer vertex(Matrix4f stack, float x, float y, float z)
        {
            if (currentColor == null) currentColor = Color.WHITE;
            ColorUtil.color(buffer.vertex(stack, x, y, z), currentColor);
            return this;
        }

        public void color(Color color)
        {
            currentColor = color;
        }

        public void color(int color)
        {
            currentColor = new Color(color);
        }

        public void draw()
        {
            if (buffer == null) return;
            if (!buffer.building) return;

            BuiltBuffer builtBuffer = this.buffer.endNullable();
            if (builtBuffer != null)
            {
                try {
                    RenderLayer layer = getRenderLayer();
                    layer.draw(builtBuffer);
                } catch (Exception e) {
                    try { builtBuffer.close(); } catch (Exception ignored) {}
                }
            }
        }

        private RenderLayer getRenderLayer()
        {
            if (drawMode == VertexFormat.DrawMode.QUADS) return QUADS_LAYER;
            if (drawMode == VertexFormat.DrawMode.LINES) return LINES_LAYER;
            if (drawMode == VertexFormat.DrawMode.DEBUG_LINES) return DEBUG_LINES_LAYER;
            if (drawMode == VertexFormat.DrawMode.TRIANGLE_FAN) return TRIANGLES_LAYER;
            return QUADS_LAYER;
        }

        public void vertexLine(double v, double v1, double v2, double v3, double v4, double v5)
        {
            vertex(v, v1, v2);
            vertex(v3, v4, v5);
        }
    }
}
