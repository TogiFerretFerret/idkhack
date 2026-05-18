package sh.idktheflag.idkhack.impl.features.hud;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idkhack.api.feature.hud.HudComponent;
import sh.idktheflag.idkhack.api.gui.GUI;
import sh.idktheflag.idkhack.api.gui.font.Fonts;
import sh.idktheflag.idkhack.api.gui.hudeditor.HudEditor;
import sh.idktheflag.idkhack.api.management.FriendManager;
import sh.idktheflag.idkhack.api.management.PopManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.math.MathUtil;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import sh.idktheflag.idkhack.api.utils.targeting.TargetUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.IdkHackMod;
import sh.idktheflag.idkhack.impl.features.modules.client.FontModule;
import sh.idktheflag.idkhack.impl.features.modules.client.HudColors;
import sh.idktheflag.idkhack.impl.gui.ClickGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.util.*;

public class TextRadar extends HudComponent
{
    public static TextRadar INSTANCE;

    Value<String> alignment = new ValueBuilder<String>()
            .withDescriptor("Alignment")
            .withValue("TopLeft")
            .withModes("TopLeft", "BottomLeft", "TopRight", "BottomRight")
            .register(this);
    Value<Boolean> spacing = new ValueBuilder<Boolean>()
            .withDescriptor("Spacing")
            .withValue(false)
            .register(this);
    Value<Boolean> autoPos = new ValueBuilder<Boolean>()
            .withDescriptor("Auto Pos")
            .withValue(true)
            .withAction(s ->
            {
                xPos.setActive(!s.getValue());
                yPos.setActive(!s.getValue());

            })
            .register(this);


    public TextRadar()
    {
        super("TextRadar");
        INSTANCE = this;
    }

    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);


        if (autoPos.getValue())
        {
            yPos.setValue(1 + Fonts.getTextHeight("A"));
            xPos.setValue(1);
        }

        if (NullUtils.nullCheck() || renderCheck(event)) return;
        if (mc.currentScreen instanceof GUI) return;


        this.width = (30);
        List<Entity> players = new ArrayList<>(TargetUtils.getPlayers().toList());


        boolean fake = mc.currentScreen instanceof HudEditor;


        if (!fake)
        {
            renderRadar(players, event.getContext());
        } else
        {
            List<String> fakePlayers = new ArrayList<>();
            fakePlayers.add("IDKFULLGUY");
            fakePlayers.add("catgirl");
            fakePlayers.add("Bait");
            renderFake(fakePlayers, event.getContext());
        }

    }

    public void renderRadar(List<Entity> players, DrawContext context)
    {
        int offset = 0;
        for (Entity player : players)
        {
            if (player == mc.player) continue;

            String text = PlayerUtils.getColoredHealth((PlayerEntity) player, true) + " ";
            if (FriendManager.INSTANCE.isFriend(player))
            {
                text += Formatting.AQUA + player.getName().getString() + Formatting.BLUE + " " + ((int) MathUtil.round(mc.player.distanceTo(player), 0));
            } else
            {
                text += Formatting.RESET + player.getName().getString() + " " + PlayerUtils.getColoredDistance((PlayerEntity) player) + ((int) MathUtil.round(mc.player.distanceTo(player), 0));
            }

            int pops = PopManager.INSTANCE.getPops(player);
            String popstring;
            if (pops < 1)
            {
                popstring = "";
            } else
            {
                popstring = Formatting.DARK_PURPLE + " -" + pops;
            }
            text += popstring;

            boolean top = alignment.getValue().contains("Top");
            if (alignment.getValue().contains("Left"))
            {

                Fonts.renderText(
                        context,
                        text,
                        xPos.getValue().intValue(),
                        yPos.getValue().intValue() + (top ? offset : -offset),
                        HudColors.getTextColor((int) (yPos.getValue().intValue() + (top ? offset : -offset))),
                        FontModule.INSTANCE.textShadow.getValue()
                );
                offset += ClickGui.CONTEXT.getRenderer().getTextHeight(text);
                if (spacing.getValue())
                    offset += 1;
            }

            if (alignment.getValue().contains("Right"))
            {
                Fonts.doOneText(
                        context,
                        text,
                        xPos.getValue().intValue() - ClickGui.CONTEXT.getRenderer().getTextWidthFloat(text),
                        yPos.getValue().intValue() + (top ? offset : -offset),
                        HudColors.getTextColor((int) (yPos.getValue().intValue() + (top ? offset : -offset))),
                        FontModule.INSTANCE.textShadow.getValue()
                );
                offset += ClickGui.CONTEXT.getRenderer().getTextHeight(text);
                if (spacing.getValue())
                    offset += 1;
            }
        }
    }


    public void renderFake(List<String> players, DrawContext context)
    {
        int offset = 0;
        int longestText = 0;
        boolean top = alignment.getValue().contains("Top");

        for (String name : players)
        {
            String text = (Objects.equals(name, "IDKFULLGUY") || Objects.equals(name, "catgirl") ? Formatting.BLUE : Formatting.GREEN) + "20 ";
            if (name.contains("IDKFULLGUY"))
            {
                text += Formatting.AQUA + name + Formatting.BLUE + " " + ((int) MathUtil.round(20, 0));
            } else
            {
                text += Formatting.RESET + name + " " + Formatting.DARK_GREEN + ((int) MathUtil.round(20, 0));
            }
            if (name.contains("IDKFULLGUY"))
            {
                text = Formatting.AQUA + (FontModule.INSTANCE.isEnabled() ? "*" : IdkHackMod.NAME_UNICODE) + " " + Formatting.RESET + text;
            }
            text += Formatting.DARK_PURPLE + " -" + 5;
            float width = ClickGui.CONTEXT.getRenderer().getTextWidthFloat(text);
            if (width > longestText)
                longestText = (int) width;
            if (alignment.getValue().contains("Left"))
            {

                Fonts.renderText(
                        context,
                        text,
                        xPos.getValue().intValue(),
                        yPos.getValue().intValue() + (top ? offset : -offset),
                        HudColors.getTextColor((int) (yPos.getValue().intValue() + (top ? offset : -offset))),
                        FontModule.INSTANCE.textShadow.getValue()
                );
                offset += ClickGui.CONTEXT.getRenderer().getTextHeight(text);
                if (spacing.getValue())
                    offset += 1;
            }

            if (alignment.getValue().contains("Right"))
            {
                Fonts.renderText(
                        context,
                        text,
                        xPos.getValue().intValue() - width,
                        yPos.getValue().intValue() + (top ? offset : -offset),
                        HudColors.getTextColor(yPos.getValue().intValue() + (top ? offset : -offset)),
                        FontModule.INSTANCE.textShadow.getValue()
                );
                offset += ClickGui.CONTEXT.getRenderer().getTextHeight(text);
                if (spacing.getValue())
                    offset += 1;
            }
        }
        height = (top ? offset : -offset);
        width = longestText;
    }

    @Override
    public String getDescription()
    {
        return "TextRadar: Display kids in your render distance";
    }
}
