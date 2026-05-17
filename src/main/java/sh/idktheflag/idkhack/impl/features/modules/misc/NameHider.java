package sh.idktheflag.idkhack.impl.features.modules.misc;


import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

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
