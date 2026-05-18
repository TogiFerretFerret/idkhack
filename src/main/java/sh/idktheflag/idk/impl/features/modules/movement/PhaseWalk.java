package sh.idktheflag.idk.impl.features.modules.movement;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.world.CollisionBoxEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import net.minecraft.util.shape.VoxelShapes;

public class PhaseWalk extends Module {
    public static PhaseWalk INSTANCE;

    public PhaseWalk() {
        super("PhaseWalk", Category.Movement);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onCollisionBox(CollisionBoxEvent event) {
        if (NullUtils.nullCheck()) return;

        if (mc.player != null && (mc.player.horizontalCollision || mc.player.isSneaking())) {
            event.setVoxelShape(VoxelShapes.empty());
            event.setCancelled(true);
        }
    }

    @Override
    public String getDescription() {
        return "PhaseWalk: Walk through blocks";
    }
}
