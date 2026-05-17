package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.world.RenderType;
import sh.idktheflag.idkhack.api.utils.world.RaytraceUtils;
import sh.idktheflag.idkhack.impl.features.modules.combat.Auto32k;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;

public class XCarry extends Module {
    public static XCarry INSTANCE;

    public XCarry()
    {
        super("XCarry", Category.Player);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event)
    {
        if (event.getPacket() instanceof CloseHandledScreenC2SPacket)
        {
            event.setCancelled(true);
        }
    }


    @Override
    public String getDescription()
    {
        return "XCarry: carry item(s) in ur holding slots like meowlauncer";
    }
}
