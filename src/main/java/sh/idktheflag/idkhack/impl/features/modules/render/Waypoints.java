package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.WaypointManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.color.IdkColor;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.world.RenderType;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
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
                
                RenderUtil.renderBox(RenderType.FILL, bb, color.getValue().getColor(), color.getValue().getColor());
                RenderUtil.renderBox(RenderType.LINES, bb, color.getValue().getColor(), color.getValue().getColor());
            }
        }
    }

    @Override
    public String getDescription() {
        return "Waypoints: Render saved waypoints in the world";
    }
}
