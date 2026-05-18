package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.PacketManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.mixin.accessor.IPlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;


public class NoFall extends Module
{

    public NoFall()
    {
        super("NoFall", Category.Player);
    }

    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Packet")
            .withModes("Packet", "Future", "AAC", "Predict", "Grim", "Latency", "NoGround")
            .register(this);
    double predictY;


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent.Pre event)
    {
        if (NullUtils.nullCheck()) return;

        if (!isFalling()) return;

        switch (mode.getValue())
        {
            case "Latency":
                if (mc.world.getRegistryKey() == World.NETHER)
                {
                    PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), 0, mc.player.getZ(), true, false));
                } else
                {
                    PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(0, 64, 0, true, false));
                }
                mc.player.fallDistance = 0.0f;
                break;
            case "Grim":
                PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() + 1.0e-9, mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), true, false));
                mc.player.onLanding();
                break;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event)
    {
        if (NullUtils.nullCheck()) return;

        if (event.isCancelled()) return;


        if (event.getPacket() instanceof PlayerMoveC2SPacket eventPacket)
        {
            IPlayerMoveC2SPacket packet = (IPlayerMoveC2SPacket) eventPacket;

            if (mode.getValue().equals("NoGround"))
            {
                packet.setOnGround(false);
                return;
            }
            if (!isFalling())
                return;

            switch (mode.getValue())
            {
                case "Packet":
                    packet.setOnGround(true);
                    break;
                case "Future":
                    packet.setY(mc.player.getY() + 0.1d);
                    break;
                case "AAC":
                    mc.player.setOnGround(true);
                    mc.player.getAbilities().flying = true;
                    mc.player.getAbilities().allowFlying = true;
                    packet.setOnGround(false);
                    mc.player.velocityDirty = true;
                    mc.player.getAbilities().flying = false;
                    mc.player.getAbilities().allowFlying = false;
                    mc.player.jump();
                    break;
                case "Predict":
                    if (predict() && mc.player.fallDistance >= 3)
                    {
                        PlayerUtils.setMotionY(0.0);
                        packet.setY(predictY);
                        mc.player.fallDistance = 0.0f;
                    }
                    break;
            }
        }
    }


    public boolean isFalling()
    {
        if (!(mc.player.fallDistance > 3.0f) || PlayerUtils.isAboveWater(mc.player) || PlayerUtils.isInWater(mc.player))
            return false;

        return true;
    }

    public boolean predict()
    {
        predictY = PlayerUtils.getGroundLevel() - 0.1;
        return mc.player.getY() - predictY < 3.0D;
    }

    @Override
    public String getHudInfo()
    {
        return mode.getValue();
    }

    @Override
    public String getDescription()
    {
        return "NoFall: Become a cat with 9(9999) lives!";
    }
}