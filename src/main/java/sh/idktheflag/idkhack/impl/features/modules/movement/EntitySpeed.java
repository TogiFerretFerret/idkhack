package sh.idktheflag.idkhack.impl.features.modules.movement;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class EntitySpeed extends Module {
    public static EntitySpeed INSTANCE;

    public EntitySpeed() {
        super("EntitySpeed", Category.Movement);
        INSTANCE = this;
    }

    Value<Number> speed = new ValueBuilder<Number>()
            .withDescriptor("Speed")
            .withValue(1.0)
            .withRange(0.1, 5.0)
            .withPlaces(2)
            .register(this);

    Value<Boolean> antiStuck = new ValueBuilder<Boolean>()
            .withDescriptor("AntiStuck")
            .withValue(true)
            .register(this);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) return;

        Entity vehicle = mc.player.getVehicle();
        if (vehicle == null) return;

        if (PlayerUtils.isMoving()) {
            double[] dir = PlayerUtils.directionSpeed(speed.getValue().doubleValue());
            vehicle.setVelocity(dir[0], vehicle.getVelocity().y, dir[1]);
        } else {
            vehicle.setVelocity(0, vehicle.getVelocity().y, 0);
        }

        if (antiStuck.getValue() && vehicle.horizontalCollision) {
            vehicle.setVelocity(vehicle.getVelocity().x, 0.1, vehicle.getVelocity().z);
        }
    }

    @Override
    public String getHudInfo() {
        return speed.getValue().toString();
    }

    @Override
    public String getDescription() {
        return "EntitySpeed: Move faster on entities";
    }
}
