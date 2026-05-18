package sh.idktheflag.idk.impl.features.hud;

import com.google.common.eventbus.Subscribe;
import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.feature.hud.HudComponent;
import sh.idktheflag.idk.api.gui.GUI;
import sh.idktheflag.idk.api.gui.font.Fonts;
import sh.idktheflag.idk.api.gui.hudeditor.HudEditor;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.impl.features.modules.client.HudColors;
import sh.idktheflag.idk.impl.features.modules.player.Blink;
import sh.idktheflag.idk.impl.gui.ClickGui;

public class BlinkDisplay extends HudComponent {
    public BlinkDisplay()
    {
        super("BlinkDisplay");
    }


    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);
        if (NullUtils.nullCheck() || renderCheck(event)) return;


        this.width = ClickGui.CONTEXT.getRenderer().getTextWidth("Currently Blinked");
        if (Blink.INSTANCE.isEnabled() || mc.currentScreen instanceof HudEditor)
        {
            Fonts.doOneText(
                    event.getContext(),
                    "Currently Blinked",
                    xPos.getValue().floatValue(),
                    yPos.getValue().floatValue(),
                    HudColors.getTextColor(yPos.getValue().intValue()),
                    ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        }
    }

    @Override
    public String getDescription()
    {
        return "BlinkDisplay: Displays if you have blink enabled";
    }
}