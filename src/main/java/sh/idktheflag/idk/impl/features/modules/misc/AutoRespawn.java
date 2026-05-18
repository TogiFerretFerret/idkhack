package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.Timer;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {

    Value<Number> delay = new ValueBuilder<Number>()
            .withDescriptor("Delay")
            .withValue(1000)
            .withRange(0, 5000)
            .register(this);

    private final Timer timer = new Timer(0);
    private boolean waiting = false;

    public AutoRespawn() {
        super("AutoRespawn", Category.Misc);
    }

    @Override
    public void onDisable() {
        waiting = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (mc.currentScreen instanceof DeathScreen) {
            if (!waiting) {
                timer.setDelay(delay.getValue().longValue());
                timer.resetDelay();
                waiting = true;
            }
            if (timer.isPassed()) {
                mc.player.requestRespawn();
                mc.setScreen(null);
                waiting = false;
            }
        } else {
            waiting = false;
        }
    }

    @Override
    public String getDescription() {
        return "AutoRespawn: automatically respawns you when you die";
    }
}
