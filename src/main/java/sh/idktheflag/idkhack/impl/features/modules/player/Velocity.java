package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.move.PushEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.PacketManager;
import sh.idktheflag.idkhack.api.management.RotationManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.mixin.accessor.IEntityVelocityUpdateS2CPacket;
import sh.idktheflag.idkhack.mixin.accessor.IExplosionS2CPacket;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Velocity extends Module
{
    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Velocity")
            .withValue("Vanilla")
            .withModes("Vanilla", "GrimV2", "Walls")
            .register(this);
    public Value<Number> horizontal = new ValueBuilder<Number>()
            .withDescriptor("Horizontal")
            .withValue(0)
            .withRange(0.0f, 100.0f)
            .withPlaces(1)
            .withPage("Vanilla")
            .withPageParent(mode)
            .register(this);
    public Value<Number> vertical = new ValueBuilder<Number>()
            .withDescriptor("Vertical")
            .withValue(0)
            .withRange(0.0f, 100.0f)
            .withPlaces(0)
            .withPage("Vanilla")
            .withPageParent(mode)
            .register(this);
    public Value<Boolean> push = new ValueBuilder<Boolean>()
            .withDescriptor("Push")
            .withValue(true)
            .register(this);
    public Value<Boolean> onlyInBlocks = new ValueBuilder<Boolean>()
            .withDescriptor("Only Blocks")
            .withValue(false)
            .register(this);
    public Value<Boolean> grim = new ValueBuilder<Boolean>()
            .withDescriptor("Grim")
            .withValue(false)
            .register(this);
    public static Velocity INSTANCE;

    public Velocity()
    {
        super("Velocity", Category.Player);
        INSTANCE = this;
    }

    int timeout = 0;
    boolean sendVelo = false;
    //    BundleS2CPacket lastBundle = null;
    boolean flag = false;

    //TODO: make this not cancel packets later!
    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event)
    {
        if (NullUtils.nullCheck()) return;


        switch (mode.getValue())
        {
            case "Vanilla":
                if (onlyInBlocks.getValue() && !false) return;



                if(grim.getValue())
                if (event.getPacket() instanceof PlayerPositionLookS2CPacket)
                {
                    flag = true;
                }
                if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet)
                {
                    if (packet.getEntityId() != mc.player.getId())
                    {
                        return;
                    }

                    // TODO: port to 1.21.11 - if (flag && packet.getVelocityX() == 0 && packet.getVelocityZ() == 0 && packet.getVelocityZ() == 0)
                    {
                        flag = false;
                        return;
                    }


                    if (horizontal.getValue().intValue() == 0 && vertical.getValue().intValue() == 0)
                    {
                        PacketManager.INSTANCE.specialCaseCancel(packet);
                        return;
                    }

                    // TODO: port to 1.21.11 - ((IEntityVelocityUpdateS2CPacket) packet).setVelocityX((int) (packet.getVelocityX() * (horizontal.getValue().floatValue() / 100.0f)));
                    // TODO: port to 1.21.11 - ((IEntityVelocityUpdateS2CPacket) packet).setVelocityY((int) (packet.getVelocityY() * (vertical.getValue().floatValue() / 100.0f)));
                    // TODO: port to 1.21.11 - ((IEntityVelocityUpdateS2CPacket) packet).setVelocityZ((int) (packet.getVelocityZ() * (horizontal.getValue().floatValue() / 100.0f)));
                }
                if (event.getPacket() instanceof ExplosionS2CPacket packet)
                {
                    if (horizontal.getValue().intValue() == 0 && vertical.getValue().intValue() == 0)
                    {
                        PacketManager.INSTANCE.specialCaseCancel(packet);
                        return;
                    }
                    // TODO: port to 1.21.11 - ((IExplosionS2CPacket) packet).setPlayerVelocityX(packet.getPlayerVelocityX() * (horizontal.getValue().floatValue() / 100.0f));
                    // TODO: port to 1.21.11 - ((IExplosionS2CPacket) packet).setPlayerVelocityY(packet.getPlayerVelocityY() * (vertical.getValue().floatValue() / 100.0f));
                    // TODO: port to 1.21.11 - ((IExplosionS2CPacket) packet).setPlayerVelocityZ(packet.getPlayerVelocityZ() * (horizontal.getValue().floatValue() / 100.0f));
                }
                break;
            case "GrimV2":
                if (event.isBundled()) return;

                if (onlyInBlocks.getValue() && !false) return;

                if (event.getPacket() instanceof PlayerPositionLookS2CPacket)
                {
                    timeout = 25;
                } else
                {
                    timeout--;
                    if (timeout >= 0) return;

                    if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet)
                    {
                        if (packet.getEntityId() != mc.player.getId())
                        {
                            return;
                        }


                        sendVelo = true;
                        PacketManager.INSTANCE.specialCaseCancel(packet);
                    }

                    if (event.getPacket() instanceof ExplosionS2CPacket packet)
                    {
                        PacketManager.INSTANCE.specialCaseCancel(packet);
                        sendVelo = true;
                    }
                }
                break;
            case "Walls":

                if (onlyInBlocks.getValue() && !false) return;

                if (event.isBundled()) return;

                if (event.getPacket() instanceof EntityDamageS2CPacket packet && packet.entityId() == mc.player.getId())
                {
                    PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false, false));
                    PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, false));
                } else if (event.getPacket() instanceof PlayerPositionLookS2CPacket)
                {
                    timeout = 25;
                } else
                {
                    timeout--;
                    if (timeout >= 0) return;

                    if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet)
                    {
                        if (packet.getEntityId() != mc.player.getId())
                        {
                            return;
                        }

                        PacketManager.INSTANCE.specialCaseCancel(packet);
                        sendVelo = true;
                    }

                    if (event.getPacket() instanceof ExplosionS2CPacket packet)
                    {
                        sendVelo = true;
                        PacketManager.INSTANCE.specialCaseCancel(packet);
                    }
                }
                break;
        }
    }

//    @SubscribeEvent
//    public void onPacket(PacketEvent.ReceivePost event)
//    {
//        if (event.getPacket().equals(lastBundle))
//            lastBundle = null;
//    }

    @SubscribeEvent
    public void onEntityPushEvent(PushEvent.Entities event)
    {
        if (push.getValue()) event.setCancelled(true);
    }


    @SubscribeEvent
    public void onBlockPushEvent(PushEvent.Blocks event)
    {
        if (push.getValue()) event.setCancelled(true);
    }

    @SubscribeEvent
    private void onTick(TickEvent.PlayerTickEvent.Pre event)
    {
        if (NullUtils.nullCheck()) return;


        flag = false;

        if (sendVelo)
        {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), RotationManager.INSTANCE.getServerYaw(), RotationManager.INSTANCE.getServerPitch(), mc.player.isOnGround(), mc.player.horizontalCollision));
            if (mc.player.isCrawling())
            {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, mc.player.getBlockPos(), Direction.DOWN));
            } else
            {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, mc.player.getBlockPos().up(), Direction.DOWN));
            }
            sendVelo = false;
        }
    }

    @Override
    public String getHudInfo()
    {
        if (mode.getValue().equals("Vanilla"))
        {
            return "H" + horizontal.getValue().floatValue() + "%" + Formatting.GRAY + "|" + Formatting.WHITE + "V" + vertical.getValue().floatValue() + "%";
        } else
        {
            return "Grim";
        }
    }

    @Override
    public String getDescription()
    {
        return "Velocity: take no knockback and become immovable from various things";
    }
}
