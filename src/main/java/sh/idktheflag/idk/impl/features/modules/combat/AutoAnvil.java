package sh.idktheflag.idk.impl.features.modules.combat;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.utils.Timer;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.utils.players.rotation.RotationUtils;
import sh.idktheflag.idk.api.utils.targeting.TargetUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.world.BlockUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.util.math.Direction;


public class AutoAnvil extends Module {
    Timer timer = new Timer();

    Value<Number> delay = new ValueBuilder<Number>()
            .withDescriptor("Delay")
            .withValue(250)
            .withRange(250, 1500)
            .withAction(set -> timer.setDelay(set.getValue().longValue()))
            .register(this);
    Value<Boolean> rotate = new ValueBuilder<Boolean>()
            .withDescriptor("Rotate")
            .withValue(true)
            .register(this);
    Value<Boolean> strictDirection = new ValueBuilder<Boolean>()
            .withDescriptor("Strict Direction")
            .withValue(false)
            .register(this);

    public AutoAnvil()
    {
        super("AutoAnvil", Category.Combat);
    }

    Entity target;

    @SubscribeEvent
    public void onUpdatePre(TickEvent.PlayerTickEvent.Pre event)
    {
        if (NullUtils.nullCheck()) return;


        target = TargetUtils.getTarget(100F);
        if (target == null) return;


        int AnvilSlot = InventoryUtils.getHotbarItemSlot(Items.ANVIL);
        if (AnvilSlot == -1)
        {
            this.setEnabled(false);
            return;
        }

        if (PlayerUtils.getHighestPlaceableAnvilPos((PlayerEntity) target) != null && timer.isPassed())
        {
            BlockPos pos = PlayerUtils.getHighestPlaceableAnvilPos((PlayerEntity) target);
            Direction direction;
            if ((direction = BlockUtils.getPlaceableSide(pos, strictDirection.getValue())) != null)
            {
                InventoryUtils.switchToSlot(AnvilSlot);
                doRotate(pos);
                BlockUtils.placeBlock(pos, direction, false);
                timer.resetDelay();
            }
        }


    }

    public void doRotate(BlockPos pos)
    {
        if (!rotate.getValue()) return;

        float[] rots = RotationUtils.getBlockRotations(pos, BlockUtils.getPlaceableSide(pos, strictDirection.getValue()));
        if (rots != null)
            RotationUtils.setRotation(rots);
    }

    @Override
    public String getDescription()
    {
        return "AutoAnvil: Places anvils above players at the highest possible position";
    }
}
