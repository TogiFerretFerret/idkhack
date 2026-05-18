package sh.idktheflag.idk.impl.features.modules.misc;


import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

public class NameHider extends Module {
    public static NameHider INSTANCE;
    public Value<String> replacement = new ValueBuilder<String>()
            .withDescriptor("Replace")
            .withValue("nice iq")
            .register(this);

    public NameHider()
    {
        super("NameHider", Category.Misc);
        INSTANCE = this;
    }

    @Override
    public String getDescription()
    {
        return "NameHider: be anonymous";
    }


}
