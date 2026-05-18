package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.PacketManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.players.rotation.RotationUtils;
import sh.idktheflag.idk.api.utils.world.BlockUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.mixin.accessor.IMinecraftClient;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FastUse extends Module {
    private int currentItem = 0;
    Value<Number> delay = new ValueBuilder<Number>()
            .withDescriptor("Delay")
            .withValue(0)
            .withRange(0, 10)
            .withPlaces(0)
            .register(this);
    Value<Boolean> blocks = new ValueBuilder<Boolean>()
            .withDescriptor("Blocks")
            .withValue(false)
            .register(this);
    Value<Boolean> xp = new ValueBuilder<Boolean>()
            .withDescriptor("XP")
            .withValue(false)
            .register(this);
    Value<Boolean> crystals = new ValueBuilder<Boolean>()
            .withDescriptor("Crystals")
            .withValue(false)
            .register(this);
    Value<Boolean> all = new ValueBuilder<Boolean>()
            .withDescriptor("All")
            .withValue(false)
            .register(this);
    Value<Boolean> ghostFix = new ValueBuilder<Boolean>()
            .withDescriptor("Ghost Fix")
            .withValue(false)
            .register(this);
    Value<Boolean> bows = new ValueBuilder<Boolean>()
            .withDescriptor("Fast Bow")
            .withValue(false)
            .register(this);
    Value<Number> bowDelay = new ValueBuilder<Number>()
            .withDescriptor("Bow Delay")
            .withValue(3)
            .withRange(3, 25)
            .withPlaces(0)
            .register(this);
    Value<Boolean> crossBow = new ValueBuilder<Boolean>()
            .withDescriptor("Fast Crossbow")
            .withValue(false)
            .register(this);
    public static FastUse INSTANCE;

    public FastUse()
    {
        super("FastUse", Category.Player);
        INSTANCE = this;
    }


    private void handleBow()
    {
        ItemStack mainhand = mc.player.getMainHandStack();
        if (mainhand.getItem() == Items.BOW && bows.getValue())
        {

            if (mc.player.getItemUseTime() >= bowDelay.getValue().intValue())
            {
                PacketManager.INSTANCE.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
                mc.player.stopUsingItem();
            }
        } else if (crossBow.getValue() && mainhand.getItem() == Items.CROSSBOW && mc.player.getItemUseTime() / (float) CrossbowItem.getPullTime(mc.player.getMainHandStack(), mc.player) > 1.0f)
        {
            PacketManager.INSTANCE.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.stopUsingItem();
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        }
    }



    @SubscribeEvent
    public void onTick(TickEvent.AfterClientTickEvent event)
    {
        if (NullUtils.nullCheck())
        {
            return;
        }
        handleBow();
        handleItems();
    }

    public void handleItems()
    {
        if (((IMinecraftClient) mc).getItemUseCooldown() > delay.getValue().floatValue() && check(mc.player.getMainHandStack().getItem()))
        {
            if (ghostFix.getValue())
            {
                PacketManager.INSTANCE.sendPacket(id -> new PlayerInteractItemC2SPacket(mc.player.getActiveHand(), id, RotationUtils.getActualYaw(), RotationUtils.getActualPitch()));
            }
            ((IMinecraftClient) mc).setItemUseCooldown(delay.getValue().intValue());
        }
    }

    public boolean check(Item item)
    {
        return (item instanceof BlockItem && blocks.getValue())
                || (item == Items.END_CRYSTAL && crystals.getValue())
                || (item == Items.EXPERIENCE_BOTTLE && xp.getValue())
                || (all.getValue());
    }

    @Override
    public String getHudInfo()
    {
        if (NullUtils.nullCheck())
        {
            return "not loaded";
        }
        ItemStack mainhand = mc.player.getMainHandStack();
        if (mainhand.getItem() == Items.BOW)
        {
            if (mc.player.getActiveHand() != null)
            {
                return mc.player.getItemUseTimeLeft() + "";
            }
        } else
        {
            return ((IMinecraftClient) mc).getItemUseCooldown() + "";
        }
        return "";
    }

    @Override
    public String getDescription()
    {
        return "FastUse: Use items or release bows faster";
    }
}
