package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

public class Capes extends Module {
    public static Capes INSTANCE;
    public Value<String> capeMode = new ValueBuilder<String>()
            .withDescriptor("Cape Mode", "capeMode")
            .withValue("Dark")
            .withModes("Dark", "White", "Pk", "Emp", "None")
            .register(this);

    public Capes()
    {
        super("Capes", Category.Render);
        INSTANCE = this;
    }
    @Override
    public String getDescription() {
        return "Capes: Gives you cool capes that other idk users can see";
    }
}
