package sh.idktheflag.idk.impl.features.modules.movement;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.management.PacketManager;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

public class AntiVoid extends Module {

    public AntiVoid()
    {
        super("AntiVoid", Category.Movement);
    }

    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Fling")
            .withModes("Fling", "Float", "Bounce", "Packet")
            .register(this);


    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;

        BlockPos pos = mc.player.getBlockPos();
        BlockPos pos2 = new BlockPos(mc.player.getBlockX(), mc.world.getBottomY(), mc.player.getBlockZ());
        if (mc.world.getBlockState(pos2).getBlock() == Blocks.AIR)
        {
            if (pos.equals(pos2))
            {
                switch (mode.getValue())
                {
                    case "Fling":
                        PlayerUtils.setMotionY(1);
                        break;
                    case "Bounce":
                        mc.player.jump();
                        break;
                    case "Float":
                        PlayerUtils.setMotionY(0);
                        break;
                    case "Packet":
                        PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 3, mc.player.getZ(), false, false));
                        break;
                }
            }
        }
    }


    @Override
    public String getHudInfo()
    {
        return mode.getValue();
    }

    @Override
    public String getDescription()
    {
        return "AntiVoid: Prevents you from falling in the deep dark and scary void";
    }

}
