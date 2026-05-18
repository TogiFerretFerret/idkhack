package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.LivingEvent;
import sh.idktheflag.idk.api.utils.world.MineUtils;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;

import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.mixin.accessor.IClientPlayerInteractionManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;


public class AutoTool extends Module {
    public AutoTool()
    {
        super("AutoTool", Category.Misc);
    }

    @SubscribeEvent
    public void onBreakBlock(LivingEvent.AttackBlock event)
    {
        if (NullUtils.nullCheck()) return;

        BlockState state = event.getState();

        if (state == null) return;
        if (state.getBlock() != Blocks.AIR && MineUtils.canBreak(event.getPos()))
        {
            int slot = InventoryUtils.getBestToolSlot(state.getBlock());
            if (slot != -1 && mc.player.getInventory().getSelectedSlot() != slot)
            {
                mc.player.getInventory().selectedSlot = InventoryUtils.getBestToolSlot(state.getBlock());
                ((IClientPlayerInteractionManager) mc.interactionManager).doSyncSelectedSlot();
            }
        }
    }

    @Override
    public String getDescription()
    {
        return "AutoTool: Swaps to the best tool you have to mine a block";
    }
}
