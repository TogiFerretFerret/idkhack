package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.WaypointManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.utils.render.world.RenderType;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.util.math.Box;

import java.awt.*;

public class Waypoints extends Module {
    public static Waypoints INSTANCE;

    public Waypoints() {
        super("Waypoints", Category.Render);
        INSTANCE = this;
    }

    Value<IdkColor> color = new ValueBuilder<IdkColor>()
            .withDescriptor("Color")
            .withValue(new IdkColor(255, 255, 255, 100))
            .register(this);

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        for (WaypointManager.WayPoint wp : WaypointManager.INSTANCE.getWayPoints()) {
            if (wp.getDimension().equalsIgnoreCase(mc.world.getRegistryKey().getValue().getPath())) {
                Box bb = new Box(wp.getX(), wp.getY(), wp.getZ(), wp.getX() + 1, wp.getY() + 1, wp.getZ() + 1);
                
                RenderUtil.renderBox(event.getMatrices(), RenderType.FILL, bb, color.getValue().getColor(), color.getValue().getColor());
                RenderUtil.renderBox(event.getMatrices(), RenderType.LINES, bb, color.getValue().getColor(), color.getValue().getColor());
            }
        }
    }

    @Override
    public String getDescription() {
        return "Waypoints: Render saved waypoints in the world";
    }
}
