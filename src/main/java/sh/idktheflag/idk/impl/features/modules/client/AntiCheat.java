package sh.idktheflag.idk.impl.features.modules.client;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

public class AntiCheat extends Module
{

    public Value<String> acMode = new ValueBuilder<String>()
            .withDescriptor("Strict")
            .withValue("Soft")
            .withModes("Soft", "Strong")
            .register(this);
    public Value<Boolean> visualize = new ValueBuilder<Boolean>()
            .withDescriptor("Visualize")
            .withValue(true)
            .register(this);
    public Value<Boolean> strafeFix = new ValueBuilder<Boolean>()
            .withDescriptor("Strafe Fix")
            .withValue(false)
            .register(this);
    public Value<Boolean> multiTask = new ValueBuilder<Boolean>()
            .withDescriptor("Multi Fix")
            .withValue(false)
            .register(this);
    public Value<Number> boostAmount = new ValueBuilder<Number>()
            .withDescriptor("Boost")
            .withValue(0.5)
            .withRange(0.1, 1)
            .withPlaces(2)
            .register(this);
    public Value<Boolean> protocol = new ValueBuilder<Boolean>()
            .withDescriptor("Grim Protocol")
            .withValue(false)
            .register(this);
    public static AntiCheat INSTANCE;

    public AntiCheat()
    {
        super("AntiCheat", Category.Client);
        INSTANCE = this;
    }


    @Override
    public String getDescription()
    {
        return "AntiCheat: manage global anticheat settings";
    }


    public void handleMultiTask()
    {
        if (!multiTask.getValue()) return;

        if (mc.player.isUsingItem())
        {
            mc.interactionManager.stopUsingItem(mc.player);
        }
    }
}
