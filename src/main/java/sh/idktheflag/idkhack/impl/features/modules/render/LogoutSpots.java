package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.event.events.world.EntityEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.world.RenderType;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LogoutSpots extends Module {
    public static LogoutSpots INSTANCE;

    public LogoutSpots() {
        super("LogoutSpots", Category.Render);
        INSTANCE = this;
    }

    private final Map<UUID, LogoutPos> spots = new ConcurrentHashMap<>();

    Value<Sn0wColor> color = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Color")
            .withValue(new Sn0wColor(255, 0, 0, 100))
            .register(this);

    @SubscribeEvent
    public void onEntityRemove(EntityEvent.Remove event) {
        if (NullUtils.nullCheck()) return;

        if (event.getEntity() instanceof PlayerEntity player && !player.getUuid().equals(mc.player.getUuid())) {
            spots.put(player.getUuid(), new LogoutPos(player.getName().getString(), new Vec3d(player.getX(), player.getY(), player.getZ()), player.getBoundingBox()));
        }
    }

    @SubscribeEvent
    public void onEntityAdd(EntityEvent.Add event) {
        if (NullUtils.nullCheck()) return;

        if (event.getEntity() instanceof PlayerEntity player) {
            spots.remove(player.getUuid());
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        for (LogoutPos spot : spots.values()) {
            RenderUtil.renderBox(RenderType.FILL, spot.bb, color.getValue().getColor(), color.getValue().getColor());
            RenderUtil.renderBox(RenderType.LINES, spot.bb, color.getValue().getColor(), color.getValue().getColor());
            RenderUtil.drawText(spot.name + " (Logged Out)", spot.pos.add(0, spot.bb.getLengthY() + 0.5, 0), 1.5f);
        }
    }

    @Override
    public void onDisable() {
        spots.clear();
    }

    private record LogoutPos(String name, Vec3d pos, Box bb) {}

    @Override
    public String getDescription() {
        return "LogoutSpots: Show where players logged out";
    }
}
