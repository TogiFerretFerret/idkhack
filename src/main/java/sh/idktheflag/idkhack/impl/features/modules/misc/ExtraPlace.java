package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.key.MouseEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.PacketManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.world.RenderType;
import sh.idktheflag.idkhack.api.utils.world.BlockUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class ExtraPlace extends Module {
    public static ExtraPlace INSTANCE;


    Value<Boolean> fluids = new ValueBuilder<Boolean>()
            .withDescriptor("Only-Fluids")
            .withValue(true)
            .register(this);

    public Value<Boolean> grim = new ValueBuilder<Boolean>()
            .withDescriptor("Grim")
            .withValue(true)
            .register(this);
    Value<Sn0wColor> fill = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Fill")
            .withValue(new Sn0wColor(255, 62, 62, 25))
            .register(this);
    Value<Sn0wColor> line = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Line")
            .withValue(new Sn0wColor(255, 62, 62, 255))
            .register(this);

    public ExtraPlace()
    {
        super("ExtraPlace", Category.Misc);
        INSTANCE = this;
    }


    @SubscribeEvent
    public void onMouse(MouseEvent event)
    {

        if (NullUtils.nullCheck()) return;

        if (event.getButton() == 1 && event.getType() == MouseEvent.Type.CLICK)
        {
            if (mc.currentScreen != null)
                return;

            if (!(mc.player.getInventory().getSelectedStack().getItem() instanceof BlockItem)) return;

            try
            {
                if (fluids.getValue())
                {
                    if (!(mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos()).getBlock() instanceof FluidBlock))
                        return;
                } else
                {
                    if (!mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos()).isAir())
                        return;
                }

                Direction side = BlockUtils.getMineableSide(((BlockHitResult) mc.crosshairTarget).getBlockPos(), grim.getValue()).getOpposite();

                if (side == null) return;

                if (grim.getValue())
                    PacketManager.INSTANCE.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, new BlockPos(BlockPos.ZERO), Direction.UP));

//                PacketManager.INSTANCE.sendPacket(id -> new PlayerInteractBlockC2SPacket(grim.getValue() ? Hand.OFF_HAND : Hand.MAIN_HAND, (BlockHitResult) mc.crosshairTarget, id));
//                mc.player.swingHand(Hand.MAIN_HAND);


               Direction direction =  BlockUtils.getPlaceableAirplace(((BlockHitResult) mc.crosshairTarget).getBlockPos(), true);

                BlockUtils.placeBlock(((BlockHitResult) mc.crosshairTarget).getBlockPos().offset(direction.getOpposite()), direction, grim.getValue() ? Hand.OFF_HAND : Hand.MAIN_HAND, false, true);
                if (grim.getValue())
                    PacketManager.INSTANCE.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, new BlockPos(BlockPos.ZERO), Direction.UP));

                event.setCancelled(true);
            } catch (Exception ignored)
            {
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event)
    {
        if (NullUtils.nullCheck()) return;


        if (!(mc.player.getInventory().getSelectedStack().getItem() instanceof BlockItem)) return;


        try
        {
            if (fluids.getValue())
            {
                if (!(mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos()).getBlock() instanceof FluidBlock))
                    return;
            } else
            {
                if (!mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos()).isAir())
                    return;
            }


            BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();

            Direction side = BlockUtils.getMineableSide((pos), grim.getValue());

            if (side == null) return;

            Box box = new Box(pos);
            RenderUtil.renderBox(RenderType.FILL, box, fill.getValue().getColor(), fill.getValue().getColor());
            RenderUtil.renderBox(RenderType.LINES, box, line.getValue().getColor(), line.getValue().getColor());
        } catch (Exception ignored)
        {

        }
    }

    @Override
    public String getDescription()
    {
        return "ExtraPlace: allows you to place blocks in places you normally couldnt";
    }


}