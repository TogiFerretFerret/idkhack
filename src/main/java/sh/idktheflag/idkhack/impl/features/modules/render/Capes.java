package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

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
        return "Capes: Gives you cool capes that other sn0w users can see";
    }
}
