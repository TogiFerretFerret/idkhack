package sh.idktheflag.idk.api.gui.context;

import sh.idktheflag.idk.api.gui.component.IComponent;
import sh.idktheflag.idk.api.gui.helpers.MouseHelper;
import sh.idktheflag.idk.api.gui.render.IRenderer;
import sh.idktheflag.idk.api.gui.theme.IColorScheme;
import sh.idktheflag.idk.api.gui.theme.IMetrics;
import sh.idktheflag.idk.api.utils.render.ScaledResolution;
import sh.idktheflag.idk.impl.features.modules.client.gui.IdkGui;
import sh.idktheflag.idk.impl.gui.renderer.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

import java.util.ArrayList;
import java.util.List;

public class Context {

    List<IComponent> components;
    IColorScheme colorScheme;
    IMetrics metrics;
    IRenderer renderer;
    float partialTicks = 0f;
    DrawContext drawContext;
    MouseHelper helper;

    public Context(DrawContext context, IColorScheme colorScheme, IMetrics metrics, IRenderer renderer, MouseHelper helper)
    {
        drawContext = context;
        this.components = new ArrayList<>();
        this.colorScheme = colorScheme;
        this.metrics = metrics;
        this.renderer = renderer;
        this.helper = helper;
    }

    public List<IComponent> getComponents() { return components; }
    public IColorScheme getColorScheme() { return colorScheme; }
    public void setColorScheme(IColorScheme colorScheme) { this.colorScheme = colorScheme; }
    public IMetrics getMetrics() { return metrics; }
    public void setMetrics(IMetrics metrics) { this.metrics = metrics; }
    public IRenderer getRenderer() { return renderer; }
    public void setRenderer(IRenderer renderer) { this.renderer = renderer; }
    public float getPartialTicks() { return partialTicks; }
    public void setPartialTicks(float partialTicks) { this.partialTicks = partialTicks; }
    public DrawContext getDrawContext() { return drawContext; }
    public void setDrawContext(DrawContext drawContext) { this.drawContext = drawContext; }
    public MouseHelper getHelper() { return helper; }
    public void setHelper(MouseHelper helper) { this.helper = helper; }

    public IComponent getHovering(MouseHelper mouseHelper)
    {
        IComponent hovering = null;
        for (IComponent component : getComponents())
        {
            if (component.getDims().collideWithMouse(mouseHelper)) hovering = component;
        }
        return hovering;
    }
    public ScaledResolution getScaledResolution()
    {
        Window window = MinecraftClient.getInstance().getWindow();
        return new ScaledResolution(window.getScaledWidth(), window.getScaledHeight());
    }
}
