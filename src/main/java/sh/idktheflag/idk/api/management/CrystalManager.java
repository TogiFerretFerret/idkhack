package sh.idktheflag.idk.api.management;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.event.events.world.EntityEvent;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.Pair;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.IdkHackMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrystalManager implements IMinecraft {

    public static CrystalManager INSTANCE;
    public static Map<BlockPos, Pair.BoxPair> crystalBoxes = new ConcurrentHashMap<>();


    public CrystalManager()
    {
        IdkHackMod.EVENT_BUS.register(this);
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck())
        {
            crystalBoxes.clear();
            return;
        }


        for (Entity entity : mc.world.getEntities())
        {
            if (entity instanceof EndCrystalEntity crystal)
            {
                if (!entity.isAlive()) continue;

                if (mc.player.distanceTo(entity) > 16) continue;

                if (!crystalBoxes.containsKey(crystal.getBlockPos()))
                {
                    crystalBoxes.put(entity.getBlockPos(), new Pair.BoxPair(entity.getBoundingBox()));
                }
            }
        }


        long currentTime = System.currentTimeMillis();
        for (Map.Entry<BlockPos, Pair.BoxPair> entry : crystalBoxes.entrySet())
        {
            BlockPos pos = entry.getKey();
            Pair.BoxPair pair = entry.getValue();

            if (new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).squaredDistanceTo(pos.toCenterPos()) > MathUtil.square(16.0f))
            {
                crystalBoxes.remove(pos);
                return;
            }

            EndCrystalEntity crystal = null;
            for (EndCrystalEntity entity : mc.world.getNonSpectatingEntities(EndCrystalEntity.class, pair.key()))
            {
                if (entity.getBlockPos().equals(pos))
                {
                    crystal = entity;
                    break;
                }
            }
            if (crystal == null && currentTime - pair.value() > 600L)
            {
                crystalBoxes.remove(pos);
            } else if (crystal != null)
            {
                crystalBoxes.put(pos, new Pair.BoxPair(crystal.getBoundingBox()));
            }
        }
    }


    @SubscribeEvent
    public void onEntityAdd(EntityEvent.Add event)
    {
        if (event.getEntity() instanceof EndCrystalEntity entity)
        {
            if (mc.player.distanceTo(entity) > 16) return;

            crystalBoxes.put(entity.getBlockPos(), new Pair.BoxPair(entity.getBoundingBox()));
        }
    }

    public boolean isRecentlyBlocked(BlockPos pos)
    {
        Box blockBox = new Box(pos);


        for (Entity entity : mc.world.getNonSpectatingEntities(Entity.class, blockBox))
        {
            if ((entity instanceof EndCrystalEntity))
            {
                return true;
            }
        }
        for (Map.Entry<BlockPos, Pair.BoxPair> entry : crystalBoxes.entrySet())
        {
            Pair.BoxPair pair = entry.getValue();
            Box box = pair.key();
            if (box.intersects(blockBox))
            {
                return true;
            }
        }
        return false;
    }
}
