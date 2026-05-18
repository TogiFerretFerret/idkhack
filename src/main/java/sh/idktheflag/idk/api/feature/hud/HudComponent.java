package sh.idktheflag.idk.api.feature.hud;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.gui.hudeditor.HudEditorGUI;
import sh.idktheflag.idk.api.utils.render.ScaledResolution;
import sh.idktheflag.idk.api.feature.Feature;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.gui.ClickGui;

public class HudComponent extends Feature implements IMinecraft
{

    public Value<Number> xPos = new ValueBuilder<Number>()
            .withDescriptor("X Pos")
            .withValue(100)
            .withRange(0, 1000)
            .register(this);

    public Value<Number> yPos = new ValueBuilder<Number>()
            .withDescriptor("Y Pos")
            .withValue(10)
            .withRange(0, 1000)
            .register(this);

    public boolean immovable;
    protected int width = 30;
    protected int height = 9;

    public void setWidth(int width) { this.width = width; }
    public int getWidth() { return width; }
    public void setHeight(int height) { this.height = height; }
    public int getHeight() { return height; }

    public HudComponent(String name)
    {
        super(name, Category.Hud, FeatureType.Hud);
    }

    public ScaledResolution sr;

    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event)
    {

        sr = new ScaledResolution(mc);
        xPos.setMax(sr.getScaledWidth());
        yPos.setMax(sr.getScaledHeight() - ClickGui.CONTEXT.getRenderer().getTextHeight("AAA"));
    }

    public boolean renderCheck(RenderGameOverlayEvent.Text event)
    {
        return mc.currentScreen instanceof HudEditorGUI && event.getCounter() != null;
    }
}
