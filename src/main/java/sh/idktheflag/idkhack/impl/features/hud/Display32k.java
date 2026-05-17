package sh.idktheflag.idkhack.impl.features.hud;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idkhack.api.gui.font.Fonts;
import sh.idktheflag.idkhack.api.utils.color.RainbowUtil;
import sh.idktheflag.idkhack.api.utils.players.Utils32k;
import sh.idktheflag.idkhack.api.utils.render.ScaledResolution;
import sh.idktheflag.idkhack.impl.features.modules.client.HudColors;
import sh.idktheflag.idkhack.api.feature.hud.HudComponent;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.gui.ClickGui;
import net.minecraft.util.Formatting;

import java.time.ZonedDateTime;

public class Display32k extends HudComponent {
    Value<Boolean> autoPos = new ValueBuilder<Boolean>()
            .withDescriptor("Auto Pos")
            .withValue(true)
            .withAction(s ->
            {
                xPos.setActive(!s.getValue());
                yPos.setActive(!s.getValue());
            })
            .register(this);
    Value<Boolean> rainbow = new ValueBuilder<Boolean>()
            .withDescriptor("Rainbow 32k")
            .withValue(false)
            .withAction(s ->
            {
                xPos.setActive(!s.getValue());
                yPos.setActive(!s.getValue());
            })
            .register(this);

    public Display32k()
    {
        super("Display32k");
        this.time = ZonedDateTime.now();
    }

    ZonedDateTime time;

    @SubscribeEvent
    @Override
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);

        if (NullUtils.nullCheck() || renderCheck(event)) return;

        this.width = ClickGui.CONTEXT.getRenderer().getTextWidth(get32kString());
        if (NullUtils.nullCheck()) return;


        if (autoPos.getValue())
        {
            ScaledResolution sr = new ScaledResolution(mc);
            xPos.setValue((sr.getScaledWidth() - ClickGui.CONTEXT.getRenderer().getTextWidth(get32kString())) / 2);
            yPos.setValue(1 + (Welcomer.INSTANCE.isEnabled() ? ClickGui.CONTEXT.getRenderer().getTextHeight("Welcome") : 0));
        }
        if (rainbow.getValue() && Utils32k.is32kInHotbar())
        {
            RainbowUtil.renderWave(event.getContext(),
                    get32kStringNoFormat(),
                    xPos.getValue().floatValue(),
                    yPos.getValue().floatValue() + 1);
        } else
        {
            Fonts.doOneText(event.getContext(),
                    get32kString(),
                    xPos.getValue().floatValue(),
                    yPos.getValue().floatValue() + 1,
                    HudColors.getTextColor(yPos.getValue().intValue()),
                    ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        }
    }

    String get32kString()
    {
        if (Utils32k.is32kInHotbar())
        {
            return "You are" + Formatting.GREEN + " 32kfull";
        } else
        {
            return "You are" + Formatting.RED + " 32kless";
        }
    }

    String get32kStringNoFormat()
    {
        if (Utils32k.is32kInHotbar())
        {
            return "You are 32kfull";
        } else
        {
            return "You are 32kless";
        }
    }

    @Override
    public String getDescription()
    {
        return "Display32k: Tells you if you are 32kfull or not";
    }

}
