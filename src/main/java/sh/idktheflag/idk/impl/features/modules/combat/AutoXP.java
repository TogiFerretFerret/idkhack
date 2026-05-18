package sh.idktheflag.idk.impl.features.modules.combat;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.Timer;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.item.Items;

public class AutoXP extends Module {

    Value<Number> delay = new ValueBuilder<Number>()
            .withDescriptor("Delay")
            .withValue(200)
            .withRange(50, 1000)
            .register(this);

    private final Timer timer = new Timer(0);

    public AutoXP() {
        super("AutoXP", Category.Combat);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) return;
        if (!timer.isPassed()) return;

        int slot = InventoryUtils.getHotbarItemSlot(Items.EXPERIENCE_BOTTLE);
        if (slot == -1) return;

        PlayerUtils.switchAndUse(slot);
        timer.setDelay(delay.getValue().longValue());
        timer.resetDelay();
    }

    @Override
    public String getDescription() {
        return "AutoXP: automatically throws experience bottles from your hotbar";
    }
}
