package sh.idktheflag.idk.impl.features.modules.combat;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.world.BlockUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Surround extends Module {
    public static Surround INSTANCE;

    public Surround() {
        super("Surround", Category.Combat);
        INSTANCE = this;
    }

    Value<Boolean> center = new ValueBuilder<Boolean>()
            .withDescriptor("Center")
            .withValue(true)
            .register(this);

    @Override
    public void onEnable() {
        if (NullUtils.nullCheck()) return;

        if (center.getValue()) {
            double x = Math.floor(mc.player.getX()) + 0.5;
            double z = Math.floor(mc.player.getZ()) + 0.5;
            mc.player.setPosition(x, mc.player.getY(), z);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) return;

        int obiSlot = InventoryUtils.findBlockInHotbar(Blocks.OBSIDIAN);
        if (obiSlot == -1) return;

        BlockPos playerPos = mc.player.getBlockPos();
        BlockPos[] surroundPos = {
            playerPos.north(),
            playerPos.south(),
            playerPos.east(),
            playerPos.west()
        };

        int oldSlot = mc.player.getInventory().selectedSlot;
        for (BlockPos pos : surroundPos) {
            if (mc.world.getBlockState(pos).isReplaceable()) {
                InventoryUtils.switchToSlot(obiSlot);
                BlockUtils.placeBlock(pos, BlockUtils.getPlaceableSide(pos, true), false);
            }
        }
        InventoryUtils.switchToSlot(oldSlot);
    }

    @Override
    public String getDescription() {
        return "Surround: Automatically place obsidian around your feet";
    }
}
