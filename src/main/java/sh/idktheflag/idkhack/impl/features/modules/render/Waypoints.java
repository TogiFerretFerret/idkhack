package sh.idktheflag.idkhack.impl.features.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.feature.module.Category;
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

// TODO: port to 1.21.11 - module disabled, see TODO.md
public class Waypoints extends Module {
    public Waypoints() {
        super("Waypoints", Category.Render);
    }
}
