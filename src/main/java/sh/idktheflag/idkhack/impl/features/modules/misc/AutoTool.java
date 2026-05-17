package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.LivingEvent;
import sh.idktheflag.idkhack.api.utils.world.MineUtils;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;

import sh.idktheflag.idkhack.api.utils.players.InventoryUtils;
import sh.idktheflag.idkhack.mixin.accessor.IClientPlayerInteractionManager;
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
                // TODO: port to 1.21.11 - mc.player.getInventory().getSelectedSlot() = InventoryUtils.getBestToolSlot(state.getBlock());
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
