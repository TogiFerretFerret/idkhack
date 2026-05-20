package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.awt.Color;

public class Skeleton extends Module {
    public static Skeleton INSTANCE;

    public Skeleton() {
        super("Skeleton", Category.Render);
        INSTANCE = this;
    }

    public Value<IdkColor> color = new ValueBuilder<IdkColor>()
            .withDescriptor("Color")
            .withValue(new IdkColor(255, 255, 255, 255))
            .register(this);

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || !player.isAlive() || player.isInvisible()) continue;
            
            renderSkeleton(event, player);
        }
    }

    private void renderSkeleton(RenderWorldEvent event, PlayerEntity player) {
        Vec3d center = player.getLerpedPos(event.getTickDelta()).add(0, player.getHeight() / 2, 0);
        double height = player.getHeight();
        
        Color c = color.getValue().getColor();
        
        // Spine
        Vec3d head = center.add(0, height / 2 - 0.2, 0);
        Vec3d pelvis = center.subtract(0, 0.2, 0);
        RenderUtil.drawWorldLine(event.getMatrices(), head, pelvis, c, c);
        
        // Shoulders
        Vec3d shoulderL = head.subtract(0.4, 0.2, 0);
        Vec3d shoulderR = head.add(0.4, -0.2, 0);
        RenderUtil.drawWorldLine(event.getMatrices(), shoulderL, shoulderR, c, c);
        
        // Arms (static for now)
        RenderUtil.drawWorldLine(event.getMatrices(), shoulderL, shoulderL.subtract(0, 0.6, 0), c, c);
        RenderUtil.drawWorldLine(event.getMatrices(), shoulderR, shoulderR.subtract(0, 0.6, 0), c, c);
        
        // Pelvis to Legs
        Vec3d hipL = pelvis.subtract(0.2, 0, 0);
        Vec3d hipR = pelvis.add(0.2, 0, 0);
        RenderUtil.drawWorldLine(event.getMatrices(), hipL, hipR, c, c);
        
        // Legs (static for now)
        RenderUtil.drawWorldLine(event.getMatrices(), hipL, hipL.subtract(0, height / 2, 0), c, c);
        RenderUtil.drawWorldLine(event.getMatrices(), hipR, hipR.subtract(0, height / 2, 0), c, c);
    }

    @Override
    public String getDescription() {
        return "Skeleton: Render a stick figure over players";
    }
}
