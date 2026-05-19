package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.FriendManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.ColorUtil;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.render.Interpolator;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.utils.world.EntityUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.features.modules.client.HudColors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

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

    Value<IdkColor> playerColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Player Color")
            .withValue(new IdkColor(255, 255, 255))
            .register(this);

    Value<IdkColor> mobColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Mob Color")
            .withValue(new IdkColor(255, 0, 0))
            .register(this);

    Value<IdkColor> animalColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Animal Color")
            .withValue(new IdkColor(0, 255, 0))
            .register(this);

    Value<IdkColor> itemColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Item Color")
            .withValue(new IdkColor(0, 128, 255))
            .register(this);

    public Tracers() {
        super("Tracers", Category.Render);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        float tickDelta = mc.getRenderTickCounter().getTickProgress(false);
        Vec3d from = mc.gameRenderer.getCamera().getCameraPos();

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player || !entity.isAlive()) continue;

            Color color = getColor(entity);
            if (color == null) continue;

            if (friendsOnly.getValue() && entity instanceof PlayerEntity player) {
                if (!FriendManager.INSTANCE.isFriend(player)) continue;
            }

            Box bb = Interpolator.getInterpolatedEntityBox(entity);
            Vec3d target = bb.getCenter();

            drawTracer(event.getMatrices(), from, target, color);
        }
    }

    private void drawTracer(MatrixStack matrices, Vec3d from, Vec3d to, Color color) {
        RenderUtil.drawWorldLine(matrices, from, to, ColorUtil.newAlpha(color, 255), ColorUtil.newAlpha(color, 180));
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
