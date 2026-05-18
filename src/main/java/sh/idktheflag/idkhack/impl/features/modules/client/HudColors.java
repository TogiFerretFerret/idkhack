package sh.idktheflag.idkhack.impl.features.modules.client;

import sh.idktheflag.idkhack.api.utils.math.MathUtil;
import sh.idktheflag.idkhack.api.utils.color.RainbowUtil;
import sh.idktheflag.idkhack.api.utils.color.IdkColor;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.IdkHackMod;

import java.awt.*;

public class HudColors extends Module {


    public Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode", "colorMode")
            .withValue("Static")
            .withModes("Static", "Step", "TriStep", "Rainbow")
            .withAction(s -> handlePage(s.getValue()))
            .register(this);
    public Value<Number> saturation = new ValueBuilder<Number>()
            .withDescriptor("Saturation")
            .withValue(108)
            .withRange(1, 255)
            .register(this);
    public Value<Number> brightness = new ValueBuilder<Number>()
            .withDescriptor("Brightness")
            .withValue(255)
            .withRange(1, 255)
            .register(this);
    public Value<Number> rainbowLength = new ValueBuilder<Number>()
            .withDescriptor("Rainbow Length")
            .withValue(35)
            .withRange(1, 100)
            .register(this);
    public Value<Number> rainbowSpeed = new ValueBuilder<Number>()
            .withDescriptor("Rainbow Speed")
            .withValue(1)
            .withPlaces(1)
            .withRange(0.1, 4)
            .register(this);
    public Value<Number> stepLength = new ValueBuilder<Number>()
            .withDescriptor("Step Length")
            .withValue(0.1)
            .withRange(0.1, 2)
            .register(this);
    public Value<Number> stepSpeed = new ValueBuilder<Number>()
            .withDescriptor("Step Speed")
            .withValue(1)
            .withRange(1, 10)
            .withPlaces(1)
            .register(this);
    Value<IdkColor> mainColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Main Color")
            .withValue(new IdkColor(0, 150, 255, true))
            .register(this);
    Value<IdkColor> stepColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Step Color")
            .withValue(new IdkColor(0, 150, 255, true))
            .register(this);
    Value<IdkColor> endColor = new ValueBuilder<IdkColor>()
            .withDescriptor("End Color")
            .withValue(new IdkColor(0, 150, 255, true))
            .register(this);

    public static HudColors INSTANCE;

    public HudColors()
    {
        super("HudColors", Category.Client);
        INSTANCE = this;
    }

    public void handlePage(String page)
    {
        mainColor.setActive(page.equals("Static") || page.equals("Step") || page.equals("TriStep"));
        saturation.setActive(page.equals("Rainbow"));
        brightness.setActive(page.equals("Rainbow"));
        rainbowSpeed.setActive(page.equals("Rainbow"));

        rainbowLength.setActive(page.equals("Rainbow"));
        stepLength.setActive(page.equals("Step") || page.equals("TriStep"));
        stepSpeed.setActive(page.equals("Step") || page.equals("TriStep"));
        stepColor.setActive(page.equals("Step") || page.equals("TriStep"));
        endColor.setActive(page.equals("TriStep"));
    }

    /**
     * gets text color for y pos
     */
    public static Color getTextColor(int y)
    {
        switch (INSTANCE.mode.getValue())
        {
            case "Step":
                double roundY = Math.cos(Math.toRadians((y / INSTANCE.stepLength.getValue().floatValue()) + ((System.currentTimeMillis() * 0.1) * INSTANCE.stepSpeed.getValue().floatValue())));


                return ColorUtil.interpolate((float) MathUtil.normalize(roundY, -1.0, 1), INSTANCE.mainColor.getValue().getColor(), INSTANCE.stepColor.getValue().getColor());
            case "TriStep":
                float realOffset = (float) ((Math.toRadians(y / (INSTANCE.stepLength.getValue().floatValue() * 2)) + ((System.currentTimeMillis() - IdkHackMod.START_TIME) * 0.0001) * (INSTANCE.stepSpeed.getValue().floatValue() * 2)));
                return ColorUtil.realInterp(realOffset % 1.5f, INSTANCE.mainColor.getValue().getColor(), INSTANCE.stepColor.getValue().getColor(), INSTANCE.endColor.getValue().getColor());

//                return ColorUtil.interpo late((float) MathUtil.normalize(roundY, -1.0, 1), INSTANCE.mainColor.getValue().getColor(), INSTANCE.stepColor.getValue().getColor(), INSTANCE.endColor.getValue().getColor());
            case "Rainbow":
                return RainbowUtil.effect((long) -(y * (INSTANCE.rainbowLength.getValue().doubleValue() * 10000)), INSTANCE.saturation.getValue().floatValue() / 255, INSTANCE.brightness.getValue().floatValue() / 255);
            case "Static":
                return INSTANCE.mainColor.getValue().getColor();
        }
        return INSTANCE.mainColor.getValue().getColor();
    }

    @Override
    public String getDescription()
    {
        return "HudColors: edit the hud colors";
    }

}
