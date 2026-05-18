package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.FriendManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.utils.render.Interpolator;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.world.EntityUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.features.modules.client.HudColors;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

import static sh.idktheflag.idkhack.api.utils.render.world.buffers.RenderBuffers.*;

public class Tracers extends Module {

    Value<Boolean> players = new ValueBuilder<Boolean>()
            .withDescriptor("Players")
            .withValue(true)
            .register(this);

    Value<Boolean> mobs = new ValueBuilder<Boolean>()
            .withDescriptor("Mobs")
            .withValue(false)
            .register(this);

    Value<Boolean> animals = new ValueBuilder<Boolean>()
            .withDescriptor("Animals")
            .withValue(false)
            .register(this);

    Value<Boolean> items = new ValueBuilder<Boolean>()
            .withDescriptor("Items")
            .withValue(false)
            .register(this);

    Value<Boolean> friendsOnly = new ValueBuilder<Boolean>()
            .withDescriptor("Friends Only")
            .withValue(false)
            .register(this);

    Value<Boolean> colorByType = new ValueBuilder<Boolean>()
            .withDescriptor("Color By Type")
            .withValue(true)
            .register(this);

    Value<Sn0wColor> playerColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Player Color")
            .withValue(new Sn0wColor(255, 255, 255))
            .register(this);

    Value<Sn0wColor> mobColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Mob Color")
            .withValue(new Sn0wColor(255, 0, 0))
            .register(this);

    Value<Sn0wColor> animalColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Animal Color")
            .withValue(new Sn0wColor(0, 255, 0))
            .register(this);

    Value<Sn0wColor> itemColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Item Color")
            .withValue(new Sn0wColor(0, 128, 255))
            .register(this);

    public Tracers() {
        super("Tracers", Category.Render);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        float tickDelta = mc.getRenderTickCounter().getTickProgress(false);
        Vec3d eye = mc.gameRenderer.getCamera().getCameraPos();

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player || !entity.isAlive()) continue;

            Color color = getColor(entity);
            if (color == null) continue;

            if (friendsOnly.getValue() && entity instanceof PlayerEntity player) {
                if (!FriendManager.INSTANCE.isFriend(player)) continue;
            }

            Box bb = Interpolator.getInterpolatedEntityBox(entity);
            Vec3d target = bb.getCenter();

            drawTracer(eye, target, color);
        }
    }

    private void drawTracer(Vec3d from, Vec3d to, Color color) {
        MatrixStack matrices = RenderUtil.matrixFrom(from.x, from.y, from.z);
        LINES.begin(matrices.peek().getPositionMatrix());
        LINES.color(ColorUtil.newAlpha(color, 255));
        LINES.vertex(0, 0, 0);
        LINES.color(ColorUtil.newAlpha(color, 180));
        LINES.vertex(to.x - from.x, to.y - from.y, to.z - from.z);
        LINES.end();
    }

    private Color getColor(Entity entity) {
        if (entity instanceof PlayerEntity) {
            if (!players.getValue()) return null;
            if (colorByType.getValue()) return HudColors.getTextColor(0);
            return playerColor.getValue().getColor();
        }
        if (EntityUtils.isMonster(entity)) {
            if (!mobs.getValue()) return null;
            if (colorByType.getValue()) return mobColor.getValue().getColor();
            return mobColor.getValue().getColor();
        }
        if (EntityUtils.isNeutral(entity) || EntityUtils.isPassive(entity)) {
            if (!animals.getValue()) return null;
            if (colorByType.getValue()) return animalColor.getValue().getColor();
            return animalColor.getValue().getColor();
        }
        if (entity instanceof ItemEntity) {
            if (!items.getValue()) return null;
            return itemColor.getValue().getColor();
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "Tracers: draws lines to entities so you can see them through walls";
    }
}
