package sh.idktheflag.idk.api.management;

import sh.idktheflag.idk.api.event.eventbus.Priority;
import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.Timer;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.IdkHackMod;
import sh.idktheflag.idk.impl.features.modules.client.AntiCheat;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;

public class BoostManager implements IMinecraft {

    public static BoostManager INSTANCE;

    Timer explosionTimer = new Timer();
    Timer longjumpTimer = new Timer();

    double boostExplosionSpeed;
    boolean canLongjump = false;

    public BoostManager()
    {
        IdkHackMod.EVENT_BUS.register(this);
        boostExplosionSpeed = 0;
    }

    @SubscribeEvent(Priority.MANAGER_FIRST)
    public void onPacket(PacketEvent.Receive event)
    {
        if (NullUtils.nullCheck()) return;


//        if (event.getPacket() instanceof SPacketEntityVelocity)
//        {
//            SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();
//            if (packet.getEntityID() == mc.player.getEntityId())
//            {
//                explosionTimer.resetDelay();
//                boostExplosionSpeed = Math.hypot(packet.getMotionX() / 8000D, packet.getMotionZ() / 8000D);
//            }
//        }

        if (event.getPacket() instanceof ExplosionS2CPacket packet)
        {
            Vec3d center = packet.center();
            if (new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).distanceTo(center) <= 6.0 && packet.playerKnockback().isPresent())
            {
                Vec3d knockback = packet.playerKnockback().get();
                if (knockback.x != 0 || knockback.z != 0)
                {
                    explosionTimer.resetDelay();

                    boostExplosionSpeed = Math.hypot(knockback.x, knockback.z);

                    canLongjump = true;
                    longjumpTimer.resetDelay();
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;

        explosionTimer.setDelay(400);
        longjumpTimer.setDelay(500);

        if (!PlayerUtils.isMoving())
        {
            boostExplosionSpeed = 0;
            canLongjump = false;
        }
        if (explosionTimer.isPassed())
        {
            boostExplosionSpeed = 0;
        }
        if (longjumpTimer.isPassed())
        {
            canLongjump = false;
        }

    }

    public double getBoostSpeed(boolean slow)
    {
        if (slow && boostExplosionSpeed != 0)
        {
            return AntiCheat.INSTANCE.boostAmount.getValue().floatValue();
        }

        return boostExplosionSpeed;
    }

    public boolean canDoLongjump()
    {
        return canLongjump;
    }
}
