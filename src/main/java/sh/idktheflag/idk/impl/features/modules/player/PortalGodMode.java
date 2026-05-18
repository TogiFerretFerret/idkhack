package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.gui.font.Fonts;
import sh.idktheflag.idk.api.management.PacketManager;
import sh.idktheflag.idk.api.utils.render.ScaledResolution;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.features.modules.client.FontModule;
import sh.idktheflag.idk.impl.features.modules.client.HudColors;
import sh.idktheflag.idk.impl.gui.ClickGui;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

import java.util.LinkedList;
import java.util.Queue;

public class PortalGodMode extends Module
{
    public static PortalGodMode INSTANCE;
    ScaledResolution sr;

    public PortalGodMode()
    {
        super("PortalGodMode", Category.Player);
        INSTANCE = this;
    }

    Queue<TeleportConfirmC2SPacket> tpPackets = new LinkedList<>();


    @SubscribeEvent
    public void onPacket(PacketEvent.Send event)
    {
        if (event.getPacket() instanceof TeleportConfirmC2SPacket)
        {
            this.tpPackets.add((TeleportConfirmC2SPacket) event.getPacket());
            event.setCancelled(true);
        }
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
        while (!this.tpPackets.isEmpty())
        {
            PacketManager.INSTANCE.sendPacket(tpPackets.poll());
        }
    }

    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event)
    {

        sr = new ScaledResolution(mc);


        Fonts.renderText(event.getContext(),
                "Currently In Godmode",
                ((float) sr.getScaledWidth() / 2) - (Fonts.getTextWidth("Currently In Godmode") / 2),
                (sr.getScaledHeight() / 2) + 10,
                HudColors.getTextColor((sr.getScaledHeight() / 2) + 10), FontModule.INSTANCE.textShadow.getValue());
    }

    @Override
    public String getDescription()
    {
        return "PortalGodMode: Makes u invincible when entering portals";
    }
}