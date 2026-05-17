package sh.idktheflag.idkhack.api.feature.hud;

import lombok.Getter;
import lombok.Setter;
import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idkhack.api.gui.hudeditor.HudEditorGUI;
import sh.idktheflag.idkhack.api.utils.render.ScaledResolution;
import sh.idktheflag.idkhack.api.feature.Feature;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import sh.idktheflag.idkhack.impl.gui.ClickGui;

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
    @Setter
    @Getter
    protected int width = 30;
    @Setter
    @Getter
    protected int height = 9;

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
