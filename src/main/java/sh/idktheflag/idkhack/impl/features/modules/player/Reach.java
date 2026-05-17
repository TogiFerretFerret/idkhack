package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.player.ReachEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.math.MathUtil;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

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
