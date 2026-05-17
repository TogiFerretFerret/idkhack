package sh.idktheflag.idkhack.impl.features.modules.client;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

public class Optimizer extends Module {


    public Value<Boolean> frustrum = new ValueBuilder<Boolean>()
            .withDescriptor("Frustrum")
            .withValue(false)
            .register(this);
    public Value<Boolean> unfocusedFPS = new ValueBuilder<Boolean>()
            .withDescriptor("Unfocused FPS")
            .withValue(false)
            .register(this);
    public Value<Number> fps = new ValueBuilder<Number>()
            .withDescriptor("Limit")
            .withValue(20)
            .withRange(1, 60)
            .withPlaces(0)
            .withParent(unfocusedFPS)
            .withParentEnabled(true)
            .register(this);
    public static Optimizer INSTANCE;

    public Optimizer()
    {
        super("Optimizer", Category.Client);
        INSTANCE = this;
    }


    @Override
    public String getDescription()
    {
        return "Optimizer: optimizes rendering and other things";
    }

}
