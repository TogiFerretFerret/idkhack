package sh.idktheflag.idk.impl.features.modules.combat;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.players.rotation.RotationUtils;
import sh.idktheflag.idk.api.utils.targeting.TargetUtils;
import sh.idktheflag.idk.api.utils.world.BlockUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.features.modules.misc.autobreak.AutoBreak;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
//made by Skitttyy 3/4/2023
//made for test

public class Lighter extends Module {

    Value<Number> targetRange = new ValueBuilder<Number>()
            .withDescriptor("Target Range")
            .withValue(10)
            .withRange(2, 15)
            .register(this);
    public Value<Boolean> strict = new ValueBuilder<Boolean>()
            .withDescriptor("Strict")
            .withValue(false)
            .register(this);

    public Value<Boolean> rotate = new ValueBuilder<Boolean>()
            .withDescriptor("Rotate")
            .withValue(false)
            .register(this);


    public Lighter()
    {
        super("Lighter", Category.Combat);
    }

    PlayerEntity target;
    BlockPos toLight = null;

    @SubscribeEvent
    public void onUpdatePre(final TickEvent.PlayerTickEvent.Pre event)
    {
        if (NullUtils.nullCheck()) return;

        if (AutoBreak.INSTANCE.didAction) return;



        if ((target = (PlayerEntity) TargetUtils.getTarget(targetRange.getValue().doubleValue())) == null) return;

        int flintSlot = InventoryUtils.getHotbarItemSlot(Items.FLINT_AND_STEEL);

        if (flintSlot != -1)
        {
            InventoryUtils.switchToSlot(flintSlot);

            if (BlockUtils.canIgnite(target.getBlockPos(), strict.getValue()))
            {
                if (rotate.getValue())
                    doRotate(target.getBlockPos());
                InventoryUtils.switchToSlot(flintSlot);
                toLight = target.getBlockPos();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.InputTick event)
    {
        if (NullUtils.nullCheck()) return;

        if (toLight != null)
        {
            BlockUtils.placeBlock(toLight, BlockUtils.getPlaceableSide(toLight, true), false);
            toLight = null;
        }
    }

    public void doRotate(BlockPos pos)
    {
        float[] rots = RotationUtils.getBlockRotations(pos, BlockUtils.getPlaceableSide(pos, true));
        RotationUtils.setRotation(rots);
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
        return "Lighter: lights people on fire with Flint N' Steel";
    }
}