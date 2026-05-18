package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

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

    public Value<IdkColor> color = new ValueBuilder<IdkColor>()
            .withDescriptor("Color")
            .withValue(new IdkColor(255, 255, 255, 100))
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
