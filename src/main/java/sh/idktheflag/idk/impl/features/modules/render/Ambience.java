package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

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
