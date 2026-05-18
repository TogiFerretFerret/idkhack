package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.player.ReachEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

public class Reach extends Module {
    public static Reach INSTANCE;

    public Value<Number> reach = new ValueBuilder<Number>()
            .withDescriptor("Reach")
            .withValue(0)
            .withRange(0, 3)
            .withPlaces(1)
            .register(this);

    public Reach()
    {
        super("Reach", Category.Player);
        INSTANCE = this;
    }


    @SubscribeEvent
    public void onReachEvent(ReachEvent event)
    {
        event.setReach(event.getReach() + reach.getValue().floatValue());
        event.setCancelled(true);
    }

    @Override
    public String getHudInfo()
    {
        return "+" + MathUtil.round((reach.getValue().floatValue()), 1);
    }

    @Override
    public String getDescription()
    {
        return "Reach: reach far away blocks";
    }

}
