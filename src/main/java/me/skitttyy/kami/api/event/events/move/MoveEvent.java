package me.skitttyy.kami.api.event.events.move;

import lombok.Getter;
import me.skitttyy.kami.api.event.Event;
import me.skitttyy.kami.api.utils.players.PlayerUtils;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

/**
 * @see me.skitttyy.kami.mixin.MixinClientPlayerEntity
 */
@Getter
public class MoveEvent extends Event {
    private final MovementType type;
    private double x, y, z;

    public MoveEvent(MovementType type, Vec3d movement)
    {
        this.type = type;
        this.x = movement.x;
        this.y = movement.y;
        this.z = movement.z;
    }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setZ(double z) { this.z = z; }

    public Vec3d getMovement()
    {
        return new Vec3d(x, y, z);
    }


    public void motionX(double x)
    {
        PlayerUtils.setMotionX(x);
        setX(x);
    }
    public void motionY(double y)
    {
        PlayerUtils.setMotionY(y);
        setY(y);
    }

    public void motionZ(double z)
    {
        PlayerUtils.setMotionZ(z);
        setZ(z);
    }

    public void setMovement(Vec3d vec3d)
    {
        setX(vec3d.x);
        setY(vec3d.y);
        setZ(vec3d.z);


    }
}
