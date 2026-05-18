package sh.idktheflag.idkhack.impl.features.modules.combat;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.Timer;
import sh.idktheflag.idkhack.api.utils.players.InventoryUtils;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
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
