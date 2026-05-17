package sh.idktheflag.idkhack.impl.features.modules.movement;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class FastClimb extends Module {

    public static FastClimb INSTANCE;

    public FastClimb() {
        super("FastClimb", Category.Movement);
        INSTANCE = this;
    }


    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent.Pre event) {
        if (NullUtils.nullCheck()) return;


        if (mc.player.input.getMovementInput().y > 0.01
                && mc.player.isOnGround()
                && mc.world.getBlockState(new BlockPos((int) mc.player.getX(), (int) (mc.player.getY() - 1), (int) mc.player.getZ())).getBlock() instanceof StairsBlock) {

            mc.player.jump();
        }
    }


    @Override
    public String getDescription() {
        return "FastClimb: Climbs up stairs fast";
    }
}
