package sh.idktheflag.idk.impl.features.hud;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.gui.GUI;
import sh.idktheflag.idk.api.gui.font.Fonts;
import sh.idktheflag.idk.api.utils.color.RainbowUtil;
import sh.idktheflag.idk.api.utils.render.ScaledResolution;
import sh.idktheflag.idk.impl.features.modules.client.FontModule;
import sh.idktheflag.idk.impl.features.modules.client.HudColors;
import sh.idktheflag.idk.api.feature.hud.HudComponent;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.IdkHackMod;
import sh.idktheflag.idk.impl.features.modules.misc.NameHider;
import sh.idktheflag.idk.impl.gui.ClickGui;
import net.minecraft.util.Formatting;

import java.time.ZonedDateTime;

public class Welcomer extends HudComponent {
    Value<Boolean> autoPos = new ValueBuilder<Boolean>()
            .withDescriptor("Auto Pos")
            .withValue(true)
            .withAction(s ->
            {
                xPos.setActive(!s.getValue());
                yPos.setActive(!s.getValue());
            })
            .register(this);

    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Extra")
            .withModes("Extra", "No Extra", "Kami5", "Only", "cats")
            .register(this);
    Value<Boolean> wheelChair = new ValueBuilder<Boolean>()
            .withDescriptor("WheelChair")
            .withValue(false)
            .register(this);
    Value<Boolean> wave = new ValueBuilder<Boolean>()
            .withDescriptor("Wave")
            .withValue(false)
            .register(this);
    Value<Boolean> whiteName = new ValueBuilder<Boolean>()
            .withDescriptor("White")
            .withValue(false)
            .register(this);
    public static Welcomer INSTANCE;

    public Welcomer()
    {
        super("Welcomer");
        INSTANCE = this;
        this.time = ZonedDateTime.now();
    }

    ZonedDateTime time;

    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);
        if (NullUtils.nullCheck() || renderCheck(event)) return;

        if (mc.currentScreen instanceof GUI) return;

        this.width = ClickGui.CONTEXT.getRenderer().getTextWidth(getWelcomeString());

        String string = getWelcomeString();

        if (autoPos.getValue())
        {
            ScaledResolution sr = new ScaledResolution(mc);
            xPos.setValue((sr.getScaledWidth() - ClickGui.CONTEXT.getRenderer().getTextWidth(string)) / 2);
            yPos.setValue(1);
        }

        if (wave.getValue())
            RainbowUtil.renderWave(event.getContext(), getWelcomeString(), xPos.getValue().floatValue(), yPos.getValue().floatValue() + 1);
        else
            Fonts.doOneText(event.getContext(), getWelcomeString(), xPos.getValue().floatValue(), yPos.getValue().floatValue() + 1, HudColors.getTextColor(yPos.getValue().intValue()), FontModule.INSTANCE.textShadow.getValue());

    }


    String getWelcomeString()
    {
        String timer = (this.time.getHour() <= 11) ? "Good Morning " : ((this.time.getHour() <= 18 && this.time.getHour() > 11) ? "Good Afternoon " : ((this.time.getHour() <= 23 && this.time.getHour() > 18) ? "Good Evening " : ""));
        String text = switch (mode.getValue())
        {
            case "Kami5" ->
                    "Welcome to " + IdkHackMod.NAME_UNICODE + Formatting.GRAY + " " + IdkHackMod.VERSION + " " + Formatting.RESET + "{NAME}";
            case "Extra" -> timer + "{NAME}" + " >:^)";
            case "No Extra" -> timer + "{NAME}";
            case "Only" -> "Welcome, {NAME}";
            case "cats" -> "Welcome, the" + Formatting.RED + "cats";
            default -> timer + "{NAME}";
        };
        if (wheelChair.getValue())
            text = text + Formatting.RESET + " \u267F";


        String name = NameHider.INSTANCE.isEnabled() ? NameHider.INSTANCE.replacement.getValue() : mc.player.getName().getString();

        text = text.replace("{NAME}", (whiteName.getValue() ? Formatting.WHITE  + name + Formatting.RESET : name));

        return text;
    }

    @Override
    public String getDescription()
    {
        return "Welcomer: Welcomes you to this elite cheat";
    }
}
