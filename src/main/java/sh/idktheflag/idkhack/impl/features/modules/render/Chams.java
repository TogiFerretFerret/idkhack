package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

public class Chams extends Module {
    public static Chams INSTANCE;

    public Chams() {
        super("Chams", Category.Render);
        INSTANCE = this;
    }

    public Value<Boolean> players = new ValueBuilder<Boolean>()
            .withDescriptor("Players")
            .withValue(true)
            .register(this);

    public Value<Boolean> crystals = new ValueBuilder<Boolean>()
            .withDescriptor("Crystals")
            .withValue(true)
            .register(this);

    public Value<Sn0wColor> color = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Color")
            .withValue(new Sn0wColor(255, 255, 255, 100))
            .register(this);

    public Value<Boolean> throughWalls = new ValueBuilder<Boolean>()
            .withDescriptor("Through Walls")
            .withValue(true)
            .register(this);

    @Override
    public String getDescription() {
        return "Chams: See entities through walls with custom colors";
    }
}
