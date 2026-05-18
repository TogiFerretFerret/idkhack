package sh.idktheflag.idk.impl.features.hud;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.gui.GUI;
import sh.idktheflag.idk.api.gui.font.Fonts;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import sh.idktheflag.idk.api.utils.color.RainbowUtil;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.utils.render.ScaledResolution;
import sh.idktheflag.idk.impl.features.modules.client.FontModule;
import sh.idktheflag.idk.impl.features.modules.client.HudColors;
import sh.idktheflag.idk.api.feature.hud.HudComponent;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.IdkHackMod;
import sh.idktheflag.idk.impl.gui.ClickGui;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Watermark extends HudComponent
{
    public static Watermark INSTANCE;

    public Watermark()
    {
        super("Watermark");
        INSTANCE = this;
    }

    Value<Boolean> wave = new ValueBuilder<Boolean>()
            .withDescriptor("Wave")
            .withValue(false)
            .register(this);
    Value<Boolean> white = new ValueBuilder<Boolean>()
            .withDescriptor("White")
            .withValue(false)
            .register(this);
    Value<Boolean> autoPos = new ValueBuilder<Boolean>()
            .withDescriptor("Auto Pos")
            .withValue(true)
            .withAction(s ->
            {
                xPos.setActive(!s.getValue());
                yPos.setActive(!s.getValue());

            })
            .register(this);

    @SubscribeEvent
    @Override
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);

        if (autoPos.getValue())
        {
            yPos.setValue(1);
            xPos.setValue(1);
        }

        if (NullUtils.nullCheck() || renderCheck(event)) return;


        if (mc.currentScreen instanceof GUI) return;


        this.width = ClickGui.CONTEXT.getRenderer().getTextWidth(getClientName());
        this.height = ClickGui.CONTEXT.getRenderer().getTextHeight("AAA");
        if (wave.getValue())
            RainbowUtil.renderWave(event.getContext(), getClientName(), xPos.getValue().floatValue(), yPos.getValue().floatValue());
        else
            Fonts.doOneText(event.getContext(), getClientName(), xPos.getValue().floatValue(), yPos.getValue().floatValue(), HudColors.getTextColor(yPos.getValue().intValue()), FontModule.INSTANCE.textShadow.getValue());


    }

    public String getClientName()
    {
        return (Objects.equals(IdkHackMod.NAME, IdkHackMod.NAME_UNICODE) ? "idk" : IdkHackMod.NAME) + " " + (white.getValue() ? Formatting.WHITE : "") + "v" + IdkHackMod.VERSION + "." + StringUtils.truncate(IdkHackMod.HASH, 7) + "-fabric";
    }

    @Override
    public String getDescription()
    {
        return "Watermark: Display the fact ur idkfull so idkless know whos boss";
    }

}