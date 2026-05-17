package sh.idktheflag.idkhack.impl.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.WaypointManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.color.TextSection;
import sh.idktheflag.idkhack.api.utils.render.Interpolator;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.world.buffers.RenderBuffers;
import sh.idktheflag.idkhack.impl.features.modules.client.HudColors;
import net.minecraft.client.render.Camera;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Waypoints extends Module {

    public static Waypoints INSTANCE;

    public Waypoints()
    {
        super("Waypoints", Category.Render);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event)
    {

        if(NullUtils.nullCheck()) return;
        if (!WaypointManager.INSTANCE.wayPoints.isEmpty())
        {
            RenderBuffers.scheduleRender(() ->
            {
                for (WaypointManager.WayPoint loc : WaypointManager.INSTANCE.wayPoints)
                {
                    if (loc.getName() == null) continue;

                    if ((mc.isInSingleplayer() && !loc.getServer().equals("SinglePlayer"))
                            || (mc.getNetworkHandler().getServerInfo() != null && !mc.getNetworkHandler().getServerInfo().address.contains(loc.getServer())))
                        continue;
                    if (!mc.world.getRegistryKey().getValue().getPath().equals(loc.getDimension())) continue;


                    renderWaypoint(loc, event);


                }
                // RenderSystem.enableBlend(); // TODO: port to 1.21.11

            });
        }
    }



    private void renderWaypoint(WaypointManager.WayPoint loc, RenderWorldEvent event)
    {

        Vec3d interpolate = Interpolator.getInterpolatedEyePos(mc.getCameraEntity(), event.getTickProgress());
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d pos = camera.getCameraPos();


        double dx = (pos.getX() - interpolate.getX()) - loc.getX();
        double dy = (pos.getY() - interpolate.getY()) - loc.getY();
        double dz = (pos.getZ() - interpolate.getZ()) - loc.getZ();
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

        TextSection[] text = new TextSection[1];

        String distance = Formatting.GRAY + " (" + Formatting.WHITE + MathHelper.floor(dist) + "m" + Formatting.GRAY + ")";

        text[0] = new TextSection(Formatting.WHITE + loc.getName() + distance, new Color(255, 255, 255));

        // RenderSystem.enableBlend(); // TODO: port to 1.21.11
        // RenderSystem.defaultBlendFunc(); // TODO: port to 1.21.11
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        RenderUtil.drawWaypoint(text, loc.getX(), loc.getY() + 1.4, loc.getZ(), mc.gameRenderer.getCamera(), HudColors.getTextColor(0));
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        // RenderSystem.disableBlend(); // TODO: port to 1.21.11

    }

    @Override
    public String getDescription()
    {
        return "Waypoints: renders waypoints (-waypoint create mcswag 0 64 0)";
    }
}
