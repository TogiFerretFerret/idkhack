package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.FrameEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.mixin.accessor.IHeldItemRenderer;

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
