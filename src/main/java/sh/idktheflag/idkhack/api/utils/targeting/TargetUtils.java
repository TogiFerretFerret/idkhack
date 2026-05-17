package sh.idktheflag.idkhack.api.utils.targeting;

import net.minecraft.util.math.Vec3d;
import com.google.common.collect.Streams;
import sh.idktheflag.idkhack.api.management.FriendManager;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import sh.idktheflag.idkhack.api.utils.world.HoleUtils;
import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

public class TargetUtils implements IMinecraft {

    public static LivingEntity getTarget(double targetRange)
    {
       
        return (LivingEntity)  Streams.stream(mc.world.getEntities())
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof PlayerEntity)
                .filter(TargetUtils::isAlive)
                .filter(entity -> entity.getId() != mc.player.getId())
                .filter(entity -> !FriendManager.INSTANCE.isFriend(entity))
                .filter(entity -> new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ()))<= targetRange)
                .min(Comparator.comparingDouble(entity -> new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ()))))
                .orElse(null);
    }

    public static LivingEntity getTargetFromCrystal(double targetRange, EndCrystalEntity crystal)
    {

        return (LivingEntity)  Streams.stream(mc.world.getEntities())
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof PlayerEntity)
                .filter(TargetUtils::isAlive)
                .filter(entity -> entity.getId() != mc.player.getId())
                .filter(entity -> !FriendManager.INSTANCE.isFriend(entity))
                .filter(entity -> new Vec3d(crystal.getX(), crystal.getY(), crystal.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ())) <= targetRange)
                .min(Comparator.comparingDouble(entity -> new Vec3d(crystal.getX(), crystal.getY(), crystal.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ()))))
                .orElse(null);
    }


    public static Stream<Entity> getTargets(double targetRange)
    {
        return Streams.stream(mc.world.getEntities())
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof PlayerEntity)
                .filter(TargetUtils::isAlive)
                .filter(entity -> entity.getId() != mc.player.getId())
                .filter(entity -> !FriendManager.INSTANCE.isFriend(entity))
                .filter(entity ->  new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ())) <= targetRange);
    }


    public static Stream<Entity> getPlayers()
    {
        return Streams.stream(mc.world.getEntities())
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof PlayerEntity)
                .filter(entity -> entity.getId() != mc.player.getId());
    }

    public static LivingEntity getTargetAnvil(double targetRange)
    {
        return (LivingEntity) Streams.stream(mc.world.getEntities())
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof PlayerEntity)
                .filter(TargetUtils::isAlive)
                .filter(entity -> entity.getId() != mc.player.getId())
                .filter(entity -> !FriendManager.INSTANCE.isFriend(entity))
                .filter(entity ->  new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ())) <= targetRange)
                .filter(entity -> mc.player.getY() > entity.getY())

                .min(Comparator.comparingDouble(entity ->  new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ()))))
                .orElse(null);
    }

    public static LivingEntity getTargetHolefill(double targetRange)
    {
        return (LivingEntity) Streams.stream(mc.world.getEntities())
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof PlayerEntity)
                .filter(TargetUtils::isAlive)
                .filter(entity -> entity.getId() != mc.player.getId())
                .filter(entity -> !FriendManager.INSTANCE.isFriend(entity))
                .filter(entity ->  new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ())) <= targetRange)
                .filter(entity -> !HoleUtils.isInBlock((PlayerEntity) entity))
                .filter(entity -> !HoleUtils.isHole(entity.getBlockPos()))
                .min(Comparator.comparingDouble(entity ->  new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ()))))
                .orElse(null);
    }

    public static HoleUtils.Hole getTargetHole(double targetRange)
    {
        return HoleUtils.getHoles(targetRange, mc.player.getBlockPos(), false).stream()
                .filter(hole -> mc.player.getBlockPos().getSquaredDistance(hole.pos1) <= targetRange)
                .min(Comparator.comparingDouble(hole -> mc.player.getBlockPos().getSquaredDistance(hole.pos1))).orElse(null);
    }

    public static HoleUtils.Hole getTargetHoleVec3D(double targetRange, boolean self, boolean doubles)
    {
        return HoleUtils.getHolesHolesnap(targetRange, mc.player.getBlockPos(), doubles, self).stream()
                .filter(hole -> HoleUtils.distanceTo(hole) <= targetRange)
                .min(Comparator.comparingDouble(HoleUtils::distanceTo)).orElse(null);
    }

    public static boolean isAlive(final Entity entity)
    {
        return isLiving(entity) && entity.isAlive() && ((LivingEntity) entity).getHealth() > 0.0f;
    }

    public static boolean isLiving(final Entity entity)
    {
        return entity instanceof LivingEntity;
    }

}