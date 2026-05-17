package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

public class Ambience extends Module
{
    public static Ambience INSTANCE;
    public Value<Sn0wColor> color = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Color")
            .withValue(new Sn0wColor(0, 255, 255, 255))
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
