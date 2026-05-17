package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.RotationManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.players.InventoryUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;

public class AntiDesync extends Module
{
    public static AntiDesync INSTANCE;

    public AntiDesync()
    {
        super("AntiDesync", Category.Player);
        INSTANCE = this;
    }

    public Value<Boolean> force = new ValueBuilder<Boolean>()
            .withDescriptor("No-Verify")
            .withValue(false)
            .withParentEnabled(true)
            .register(this);

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event)
    {
        if (NullUtils.nullCheck()) return;

        if (!event.isBundled())
        {
            if (force.getValue())
            {
                if (event.getPacket() instanceof ScreenHandlerSlotUpdateS2CPacket packet)
                {
                    int slot = packet.getSlot() - 36;
                    if (slot < 0 || slot > 8)
                    {
                        return;
                    }

                    if (packet.getStack().isEmpty())
                    {
                        return;
                    }

                    for (InventoryUtils.PreSwapData data : RotationManager.swapData)
                    {
                        if (data.getSlot() != slot && data.getStarting() != slot)
                        {
                            continue;
                        }

                        ItemStack preStack = data.getPreHolding(slot);
                        if (!isEqual(preStack, packet.getStack()))
                        {
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        } else
        {
            if (event.getPacket() instanceof ScreenHandlerSlotUpdateS2CPacket)
            {
                event.setCancelled(true);
            }
        }
    }

    private boolean isEqual(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem().equals(stack2.getItem()) && stack1.getName().equals(stack2.getName());
    }

    @Override
    public String getDescription()
    {
        return "AntiDesync: experimental module u probably should not use this ";
    }
}