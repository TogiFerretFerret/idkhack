package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.world.CollisionBoxEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShapes;

public class Heaven extends Module {


    public Heaven()
    {
        super("Heaven", Category.Misc);
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;

        if (mc.player.isDead())
            PlayerUtils.setMotionY(0.4);
    }

    @SubscribeEvent
    public void onCollision(CollisionBoxEvent event)
    {
        if (NullUtils.nullCheck()) return;

        if (mc.player.isDead())
        {
            event.setCancelled(true);
            event.setVoxelShape(VoxelShapes.cuboid(new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        }
    }

    @Override
    public String getDescription()
    {
        return "Heaven: get sent to heaven after u die";
    }

}
