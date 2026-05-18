package sh.idktheflag.idk.impl.features.modules.combat;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.players.rotation.RotationUtils;
import sh.idktheflag.idk.api.utils.world.BlockUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class AutoWeb extends Module {
    public static AutoWeb INSTANCE;

    public AutoWeb() {
        super("AutoWeb", Category.Combat);
        INSTANCE = this;
    }

    Value<Number> range = new ValueBuilder<Number>()
            .withDescriptor("Range")
            .withValue(5)
            .withRange(1, 6)
            .register(this);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) return;

        int webSlot = InventoryUtils.findBlockInHotbar(Blocks.COBWEB);
        if (webSlot == -1) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (mc.player.distanceTo(player) > range.getValue().floatValue()) continue;

            BlockPos pos = player.getBlockPos();
            if (BlockUtils.canPlaceBlock(pos, true)) {
                int oldSlot = mc.player.getInventory().getSelectedSlot();
                InventoryUtils.switchToSlot(webSlot);
                BlockUtils.placeBlock(pos, BlockUtils.getPlaceableSide(pos, true), false);
                InventoryUtils.switchToSlot(oldSlot);
            }
        }
    }

    @Override
    public String getDescription() {
        return "AutoWeb: Automatically place cobwebs on players";
    }
}
