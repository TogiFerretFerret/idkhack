package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.FrameEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.mixin.accessor.IHeldItemRenderer;

public class SmallShield extends Module {


    public static SmallShield INSTANCE;

    public SmallShield()
    {
        super("SmallShield", Category.Misc);
        INSTANCE = this;
    }

    public Value<Number> offset = new ValueBuilder<Number>()
            .withDescriptor("Offset")
            .withValue(1.0)
            .withRange(0.1, 3.0)
            .withPlaces(1)
            .register(this);


    @SubscribeEvent
    public void onUpdate(FrameEvent.FrameFlipEvent event)
    {
        if (NullUtils.nullCheck()) return;


        ((IHeldItemRenderer) mc.getEntityRenderDispatcher().getHeldItemRenderer()).setEquippedProgressOffHand(offset.getValue().floatValue());

    }


    @Override
    public String getDescription()
    {
        return "SmallShield: Moves your offhand downwards";
    }
}
