package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
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
