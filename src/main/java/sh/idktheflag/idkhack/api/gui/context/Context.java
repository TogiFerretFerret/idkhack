package sh.idktheflag.idkhack.api.gui.context;

import lombok.Getter;
import lombok.Setter;
import sh.idktheflag.idkhack.api.gui.component.IComponent;
import sh.idktheflag.idkhack.api.gui.helpers.MouseHelper;
import sh.idktheflag.idkhack.api.gui.render.IRenderer;
import sh.idktheflag.idkhack.api.gui.theme.IColorScheme;
import sh.idktheflag.idkhack.api.gui.theme.IMetrics;
import sh.idktheflag.idkhack.api.utils.render.ScaledResolution;
import sh.idktheflag.idkhack.impl.features.modules.client.gui.Sn0wGui;
import sh.idktheflag.idkhack.impl.gui.renderer.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
