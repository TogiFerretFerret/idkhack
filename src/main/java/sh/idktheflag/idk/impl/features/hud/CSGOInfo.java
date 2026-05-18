package sh.idktheflag.idk.impl.features.hud;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.feature.hud.HudComponent;
import sh.idktheflag.idk.api.gui.font.Fonts;
import sh.idktheflag.idk.api.management.PacketManager;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.ColorUtil;
import sh.idktheflag.idk.api.utils.color.RainbowUtil;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.targeting.TargetUtils;
import sh.idktheflag.idk.api.utils.world.HoleUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.features.modules.combat.KillAura;
import sh.idktheflag.idk.impl.gui.ClickGui;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class CSGOInfo extends HudComponent {


    public CSGOInfo()
    {
        super("PvpInfo");
    }

    public Value<IdkColor> popColorA = new ValueBuilder<IdkColor>()
            .withDescriptor("Color A")
            .withValue(new IdkColor(0, 255, 255, 255))
            .register(this);
    public Value<IdkColor> popColorB = new ValueBuilder<IdkColor>()
            .withDescriptor("Color B")
            .withValue(new IdkColor(0, 0, 255, 255))
            .register(this);
    public Value<IdkColor> popColorC = new ValueBuilder<IdkColor>()
            .withDescriptor("Color C")
            .withValue(new IdkColor(87, 8, 97))
            .register(this);

    @SubscribeEvent
    @Override
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);

        if (NullUtils.nullCheck() || renderCheck(event)) return;


        int height = (int) Fonts.getTextHeight("A");
        int i = 0;
        RainbowUtil.renderWave(event.getContext(), "catogod.cc", (float) xPos.getValue().intValue(), (float) yPos.getValue().intValue());
        PlayerEntity opp = (PlayerEntity) TargetUtils.getTarget(30);
        i += height;

        if (opp != null)
        {
            Fonts.doOneText(event.getContext(), "HTR", xPos.getValue().intValue(), yPos.getValue().intValue() + i, (mc.player.getEyePos().distanceTo(opp.getEyePos()) <= KillAura.INSTANCE.range.getValue().floatValue() ? Color.GREEN : Color.RED), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
            i += height;
            Fonts.doOneText(event.getContext(), "PLR", xPos.getValue().intValue(), yPos.getValue().intValue() + i, getHoleColor(opp), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
            i += height;
            if (opp.hasStatusEffect(StatusEffects.WEAKNESS))
            {
                Fonts.renderText(event.getContext(), "WKN", xPos.getValue().intValue(), yPos.getValue().intValue() + i, Color.GREEN, ClickGui.CONTEXT.getColorScheme().doesTextShadow());
                i += height;

            }
        } else
        {
            Fonts.doOneText(event.getContext(), "HTR", xPos.getValue().intValue(), yPos.getValue().intValue() + i, Color.RED, ClickGui.CONTEXT.getColorScheme().doesTextShadow());
            i += height;
            Fonts.doOneText(event.getContext(), "PLR", xPos.getValue().intValue(), yPos.getValue().intValue() + i, Color.RED, ClickGui.CONTEXT.getColorScheme().doesTextShadow());
            i += height;
        }
        int totems = InventoryUtils.getItemCount(Items.TOTEM_OF_UNDYING);

        Fonts.doOneText(event.getContext(), totems + "", xPos.getValue().intValue(), yPos.getValue().intValue() + i, getPopColor(totems), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        i += height;
        Fonts.doOneText(event.getContext(), "PING " +  PacketManager.INSTANCE.getClientLatency(), xPos.getValue().intValue(), yPos.getValue().intValue() + i, (PacketManager.INSTANCE.getClientLatency() <= 100 ? Color.GREEN : Color.RED), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        i += height;
        Fonts.doOneText(event.getContext(), "LBY", xPos.getValue().intValue(), yPos.getValue().intValue() + i, mc.player.getY() <= mc.world.getBottomY() + 1.0f ? Color.GREEN : Color.RED, ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        this.width = ClickGui.CONTEXT.getRenderer().getTextWidth("catogod.cc");
        this.height = i + ClickGui.CONTEXT.getRenderer().getTextHeight("A");
    }

    public Color getHoleColor(PlayerEntity opp)
    {
        BlockPos pos = opp.getBlockPos();
        if (HoleUtils.isHole(pos))
        {
            if (HoleUtils.isObbyHole(pos))
            {
                return Color.ORANGE;
            } else if (HoleUtils.isBedrockHoles(pos))
            {
                return Color.GREEN;
            }
            return Color.RED;
        } else
        {
            return Color.RED;
        }
    }

    public Color getPopColor(int pops)
    {
        if (pops == 0) return Color.RED;


        if (pops < 5)
        {
            return ColorUtil.interpolate((float) MathHelper.clamp(MathUtil.normalize(pops, 1, 5), 0, 1), popColorB.getValue().getColor(), popColorA.getValue().getColor());
        } else
        {
            return ColorUtil.interpolate((float) MathHelper.clamp(MathUtil.normalize(pops, 5, 10), 0, 1), popColorC.getValue().getColor(), popColorB.getValue().getColor());
        }
    }

    @Override
    public String getDescription()
    {
        return "PvpInfo: nn thinks hes elite but he can be elite with this module";
    }
}
