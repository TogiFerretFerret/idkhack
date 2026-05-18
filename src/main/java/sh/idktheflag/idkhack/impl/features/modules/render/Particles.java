package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.color.IdkColor;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

public class Particles extends Module {


    public Value<IdkColor> colorOne = new ValueBuilder<IdkColor>()
            .withDescriptor("Color")
            .withValue(new IdkColor(255, 255, 255))
            .register(this);
    public Value<Boolean> doubleColor = new ValueBuilder<Boolean>()
            .withDescriptor("Two Color")
            .withValue(false)
            .register(this);
    public Value<IdkColor> colorTwo = new ValueBuilder<IdkColor>()
            .withDescriptor("Second Color")
            .withValue(new IdkColor(0, 255, 72))
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
