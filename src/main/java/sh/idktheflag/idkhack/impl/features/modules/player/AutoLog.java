package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.text.Text;

public class AutoLog extends Module {
    public static AutoLog INSTANCE;

    public AutoLog() {
        super("AutoLog", Category.Player);
        INSTANCE = this;
    }

    Value<Number> health = new ValueBuilder<Number>()
            .withDescriptor("Health")
            .withValue(10)
            .withRange(1, 20)
            .withPlaces(1)
            .register(this);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) return;

        if (mc.player.getHealth() <= health.getValue().floatValue()) {
            mc.getNetworkHandler().getConnection().disconnect(Text.of("AutoLog: Health low (" + mc.player.getHealth() + ")"));
            toggle();
        }
    }

    @Override
    public String getDescription() {
        return "AutoLog: Disconnect when health is low";
    }
}
