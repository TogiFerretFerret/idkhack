package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

public class Particles extends Module {


    public Value<Sn0wColor> colorOne = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Color")
            .withValue(new Sn0wColor(255, 255, 255))
            .register(this);
    public Value<Boolean> doubleColor = new ValueBuilder<Boolean>()
            .withDescriptor("Two Color")
            .withValue(false)
            .register(this);
    public Value<Sn0wColor> colorTwo = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Second Color")
            .withValue(new Sn0wColor(0, 255, 72))
            .withParent(doubleColor)
            .withParentEnabled(true)
            .register(this);
    public static Particles INSTANCE;

    public Particles()
    {
        super("Particles", Category.Render);
        INSTANCE = this;
    }

    @Override
    public String getDescription()
    {
        return "Particles: change the colors of various particles";
    }
}
