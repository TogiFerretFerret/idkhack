package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.color.IdkColor;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

public class Ambience extends Module
{
    public static Ambience INSTANCE;
    public Value<IdkColor> color = new ValueBuilder<IdkColor>()
            .withDescriptor("Color")
            .withValue(new IdkColor(0, 255, 255, 255))
            .register(this);

    public Ambience()
    {
        super("Ambience", Category.Render);
        INSTANCE = this;
    }



    @Override
    public String getDescription()
    {
        return "Ambience: colors the world a different color";
    }

}
