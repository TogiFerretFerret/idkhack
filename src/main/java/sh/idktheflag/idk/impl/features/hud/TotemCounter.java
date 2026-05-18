package sh.idktheflag.idk.impl.features.hud;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.feature.hud.HudComponent;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.utils.render.ScaledResolution;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.features.modules.client.HudColors;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.awt.*;

public class TotemCounter extends HudComponent {
    public TotemCounter()
    {
        super("TotemCounter");
    }

    Value<Boolean> autoPos = new ValueBuilder<Boolean>()
            .withDescriptor("Auto Pos")
            .withValue(true)
            .withAction(s ->
            {
                xPos.setActive(!s.getValue());
                yPos.setActive(!s.getValue());
            })
            .register(this);
    public Value<IdkColor> textColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Text Color")
            .withValue(new IdkColor(255, 255, 255))
            .register(this);

    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);
        if (NullUtils.nullCheck() || renderCheck(event)) return;


        this.width = 16;
        this.height = 16;
        ScaledResolution sr = new ScaledResolution(mc);


        if (autoPos.getValue())
        {
            int i = sr.getScaledWidth() / 2;
            xPos.setValue(i - 189 + 180 + 2);
            yPos.setValue(sr.getScaledHeight() - 55 - (mc.player.isSubmergedInWater() ? 10 : 0));
        }

        int count = InventoryUtils.getItemCount(Items.TOTEM_OF_UNDYING);
        if (count == 0) return;

        RenderUtil.renderItemWithCount(event.getContext(), new ItemStack(Items.TOTEM_OF_UNDYING), new Point(xPos.getValue().intValue(), yPos.getValue().intValue()), count, textColor.getValue().getColor(), false);
    }

    @Override
    public String getDescription()
    {
        return "TotemCounter: displays how many totems you have";
    }

}