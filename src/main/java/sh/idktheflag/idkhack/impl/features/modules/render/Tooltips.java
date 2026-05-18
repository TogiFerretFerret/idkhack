package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

public class Tooltips extends Module {
    public static Tooltips INSTANCE;

    public Tooltips() {
        super("Tooltips", Category.Render);
        INSTANCE = this;
    }

    public Value<Boolean> shulkers = new ValueBuilder<Boolean>()
            .withDescriptor("Shulkers")
            .withValue(true)
            .register(this);

    @Override
    public String getDescription() {
        return "Tooltips: Enhanced item tooltips";
    }
}
