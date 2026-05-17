package sh.idktheflag.idkhack.impl.features.modules.ghost;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.Timer;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import sh.idktheflag.idkhack.api.utils.world.CrystalUtil;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.features.modules.client.HudColors;
import sh.idktheflag.idkhack.mixin.accessor.IHandledScreen;
import sh.idktheflag.idkhack.mixin.accessor.IMinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;

import java.awt.*;


public class AutoRetotem extends Module
{


    Value<Number> delay = new ValueBuilder<Number>()
            .withDescriptor("Delay")
            .withValue(1)
            .withRange(0, 1000)
            .register(this);
    Value<Boolean> autoRefill = new ValueBuilder<Boolean>()
            .withDescriptor("Refill")
            .withValue(false)
            .register(this);
    Value<Number> refillSlot = new ValueBuilder<Number>()
            .withDescriptor("Slot")
            .withValue(1)
            .withRange(1, 9)
            .withPlaces(0)
            .withParent(autoRefill)
            .withParentEnabled(true)
            .register(this);

    public AutoRetotem()
    {
        super("AutoRetotem", Category.Ghost);
    }

    Timer timer = new Timer();


    @SubscribeEvent
    public void onPlayerUpdate(RenderGameOverlayEvent.Text event)
    {
        if (NullUtils.nullCheck()) return;


        timer.setDelay(delay.getValue().longValue());


        if (!(mc.currentScreen instanceof InventoryScreen)) return;


        if (!timer.isPassed()) return;

        ItemStack offhandStack = mc.player.getOffHandStack();
        Slot focusedSlot = ((IHandledScreen) mc.currentScreen).getFocusedSlot();


        if (focusedSlot != null)
        {

            if (focusedSlot.id == 45) return;

            if (isTotem(focusedSlot.getStack()))
            {
                if (!isTotem(offhandStack))
                {
                    mc.interactionManager.clickSlot(((InventoryScreen) mc.currentScreen).getScreenHandler().syncId, focusedSlot.id, 40, SlotActionType.SWAP, mc.player);
                    timer.resetDelay();
                } else if (autoRefill.getValue() && !isTotem(mc.player.getInventory().getStack(refillSlot.getValue().intValue() - 1)))
                {
                    mc.interactionManager.clickSlot(((InventoryScreen) mc.currentScreen).getScreenHandler().syncId, focusedSlot.id, refillSlot.getValue().intValue() - 1, SlotActionType.SWAP, mc.player);
                    timer.resetDelay();
                }
            }
        }

    }


    private boolean isTotem(ItemStack stack)
    {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Override
    public void onEnable()
    {
        super.onEnable();
        if (NullUtils.nullCheck()) return;

    }


    @Override
    public String getDescription()
    {
        return "AutoRetotem: legit autototem";
    }
}