package sh.idktheflag.idk.impl.features.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.RenderPipelines;
import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.feature.hud.HudComponent;
import sh.idktheflag.idk.api.gui.GUI;
import sh.idktheflag.idk.api.gui.font.Fonts;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.features.modules.client.FontModule;
import sh.idktheflag.idk.mixin.accessor.IIngameHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.text.DecimalFormat;

public class HealthBar extends HudComponent
{
    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Vanilla")
            .withModes("Vanilla", "Bar", "None")
            .register(this);
    Value<String> barColor = new ValueBuilder<String>()
            .withDescriptor("Bar Color")
            .withValue("Scissor")
            .withModes("Scissor", "Custom")
            .withPageParent(mode)
            .withPage("Bar")
            .register(this);
    Value<Number> barThickness = new ValueBuilder<Number>()
            .withDescriptor("Thickness")
            .withValue(1)
            .withRange(0.5f, 15)
            .withPlaces(1)
            .withPageParent(mode)
            .withPage("Bar")
            .register(this);
    Value<IdkColor> leftColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Left Color")
            .withValue(new IdkColor(255, 0, 255, 255))
            .withPageParent(mode)
            .withPage("Bar")
            .register(this);
    Value<IdkColor> rightColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Right Color")
            .withValue(new IdkColor(0, 255, 0))
            .withPageParent(mode)
            .withPage("Bar")
            .register(this);


    Value<Boolean> text = new ValueBuilder<Boolean>()
            .withDescriptor("Text")
            .withValue(false)
            .register(this);
    Value<Boolean> autoPos = new ValueBuilder<Boolean>()
            .withDescriptor("Auto Pos")
            .withValue(true)
            .withAction(s ->
            {
                xPos.setActive(!s.getValue());
            })
            .register(this);

    public static HealthBar INSTANCE;

    public HealthBar()
    {
        super("HealthBar");
        INSTANCE = this;
    }


    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);
        if (NullUtils.nullCheck() || renderCheck(event)) return;

        if (mc.currentScreen instanceof GUI) return;


        width = 81;


        switch (mode.getValue())
        {
            case "Vanilla":
                if (autoPos.getValue())
                    xPos.setValue(((float) (event.getContext().getScaledWindowWidth() / 2) - (this.width / 2)) - 1);

                int i = MathHelper.ceil(mc.player.getHealth());
                int j = ((IIngameHud) mc.inGameHud).getRenderHealthValue();


                boolean bl = ((IIngameHud) mc.inGameHud).getHeartJumpEndTick() > (long) ((IIngameHud) mc.inGameHud).getTicks() && (((IIngameHud) mc.inGameHud).getHeartJumpEndTick() - (long) ((IIngameHud) mc.inGameHud).getTicks()) / 3L % 2L == 1L;


                float f = Math.max((float) mc.player.getAttributeValue(EntityAttributes.MAX_HEALTH), (float) Math.max(j, i));
                int o = 0;
                int p = MathHelper.ceil((f + (float) o) / 2.0F / 10.0F);
                int q = Math.max(10 - (p - 2), 3);

                int s = -1;
                if (mc.player.hasStatusEffect(StatusEffects.REGENERATION))
                {
                    s = ((IIngameHud) mc.inGameHud).getTicks() % MathHelper.ceil(f + 5.0F);
                }

                ((IIngameHud) mc.inGameHud).doRenderHealth(event.getContext(), mc.player, xPos.getValue().intValue(), yPos.getValue().intValue(), q, s, f, i, j, o, bl);
                if (text.getValue())
                {
                    Color healthColor = getHealthColor();

                    float x = xPos.getValue().intValue() + (float) width / 2;
                    float y = yPos.getValue().intValue() + 10;

                    float health = mc.player.getHealth() == 0 ? 0 : mc.player.getHealth() / 2;
                    String color = PlayerUtils.getHealthColor(mc.player, false, false) + "";

                    DecimalFormat format = new DecimalFormat("#.#");

                    String text = color + format.format(health);
                    float offset = Fonts.getTextWidth(text);


                    float totalWidth = offset + 7;
                    boolean absorption = mc.player.getAbsorptionAmount() != 0;
                    String absorptionText = "";
                    if (absorption)
                    {
                        absorptionText = Formatting.GOLD + " " + format.format(mc.player.getAbsorptionAmount() / 2);
                        totalWidth += Fonts.getTextWidth(absorptionText);
                        totalWidth += 7;

                    }


                    Fonts.doOneText(event.getContext(), text, x - (totalWidth / 2), y, healthColor, FontModule.INSTANCE.textShadow.getValue());
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                    event.getContext().drawGuiTexture(RenderPipelines.GUI_TEXTURED, InGameHud.HeartType.NORMAL.getTexture(false, false, false), (int) (x - (totalWidth / 2) + offset), (int) (y), 7, 7);
                    org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
                    offset += 7;
                    if (absorption)
                    {
                        Fonts.doOneText(event.getContext(), absorptionText, x - (totalWidth / 2) + offset, y, healthColor, FontModule.INSTANCE.textShadow.getValue());
                        offset += Fonts.getTextWidth(absorptionText);
                        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                        event.getContext().drawGuiTexture(RenderPipelines.GUI_TEXTURED, InGameHud.HeartType.ABSORBING.getTexture(false, false, false), (int) (x - (totalWidth / 2) + offset), (int) (y), 7, 7);
                        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
                    }
                }

                break;
            case "None":
                if(text.getValue())
                {
                    if (autoPos.getValue())
                        xPos.setValue(((float) (event.getContext().getScaledWindowWidth() / 2) - (this.width / 2)) - 1);

                    Color healthColor = getHealthColor();

                    float x = xPos.getValue().intValue() + (float) width / 2;
                    float y = yPos.getValue().intValue();

                    float health = mc.player.getHealth() == 0 ? 0 : mc.player.getHealth() / 2;
                    String color = PlayerUtils.getHealthColor(mc.player, false, false) + "";

                    DecimalFormat format = new DecimalFormat("#.#");

                    String text = color + format.format(health);
                    float offset = Fonts.getTextWidth(text);


                    float totalWidth = offset + 7;
                    boolean absorption = mc.player.getAbsorptionAmount() != 0;
                    String absorptionText = "";
                    if (absorption)
                    {
                        absorptionText = Formatting.GOLD + " " + format.format(mc.player.getAbsorptionAmount() / 2);
                        totalWidth += Fonts.getTextWidth(absorptionText);
                        totalWidth += 7;

                    }


                    Fonts.doOneText(event.getContext(), text, x - (totalWidth / 2), y, healthColor, FontModule.INSTANCE.textShadow.getValue());
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                    event.getContext().drawGuiTexture(RenderPipelines.GUI_TEXTURED, InGameHud.HeartType.NORMAL.getTexture(false, false, false), (int) (x - (totalWidth / 2) + offset), (int) (y), 7, 7);
                    org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
                    offset += 7;
                    if (absorption)
                    {
                        Fonts.doOneText(event.getContext(), absorptionText, x - (totalWidth / 2) + offset, y, healthColor, FontModule.INSTANCE.textShadow.getValue());
                        offset += Fonts.getTextWidth(absorptionText);
                        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                        event.getContext().drawGuiTexture(RenderPipelines.GUI_TEXTURED, InGameHud.HeartType.ABSORBING.getTexture(false, false, false), (int) (x - (totalWidth / 2) + offset), (int) (y), 7, 7);
                        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
                    }
                }
                break;
            case "Bar":


                float thickness = barThickness.getValue().floatValue();
                float percentage = Math.min(1f, mc.player.getHealth() / mc.player.getMaxHealth());

                float width = 80.0F;
                float half = width / 2;


                if (autoPos.getValue())
                    xPos.setValue(((float) (event.getContext().getScaledWindowWidth() / 2) - (width / 2)) - 1);


                float x = xPos.getValue().intValue() + half + 2;
                float y = yPos.getValue().intValue() + 5;

                Color healthColor = getHealthColor();

                // DrawContext.getMatrices() now returns Matrix3x2fStack, not MatrixStack
                net.minecraft.client.util.math.MatrixStack barMatrices = new net.minecraft.client.util.math.MatrixStack();
                RenderUtil.renderRect(barMatrices, (x - half - 0.5), (y - 0.5), (half * 2) + 1f, thickness + 1, 0x78000000);
                RenderUtil.renderGradient(barMatrices, (x - half - 0.5), (y - 0.5), width * percentage + 1, thickness + 1,
                        barColor.getValue().equals("Scissor") ? Color.RED.darker().getRGB() : leftColor.getValue().getColor().darker().getRGB(), barColor.getValue().equals("Scissor") ? healthColor.darker().getRGB() : rightColor.getValue().getColor().darker().getRGB(), true);
                RenderUtil.renderGradient(barMatrices, (x - half), y, width * percentage, thickness,
                        barColor.getValue().equals("Scissor") ? Color.RED.getRGB() : leftColor.getValue().getColor().getRGB(), barColor.getValue().equals("Scissor") ? healthColor.getRGB() : rightColor.getValue().getColor().getRGB(), true);


                if (text.getValue())
                {
                    float health = mc.player.getHealth() == 0 ? 0 : mc.player.getHealth() / 2;
                    String color = PlayerUtils.getHealthColor(mc.player, false, false).toString();

                    DecimalFormat format = new DecimalFormat("#.#");

                    String text = color + format.format(health);
                    float offset = Fonts.getTextWidth(text);


                    float totalWidth = offset + 7;
                    boolean absorption = mc.player.getAbsorptionAmount() != 0;
                    String absorptionText = "";
                    if (absorption)
                    {
                        absorptionText = Formatting.GOLD + " " + format.format(mc.player.getAbsorptionAmount() / 2);
                        totalWidth += Fonts.getTextWidth(absorptionText);
                        totalWidth += 7;

                    }


                    Fonts.doOneText(event.getContext(), text, x - (totalWidth / 2), y + thickness + 4, healthColor, FontModule.INSTANCE.textShadow.getValue());
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                    event.getContext().drawGuiTexture(RenderPipelines.GUI_TEXTURED, InGameHud.HeartType.NORMAL.getTexture(false, false, false), (int) (x - (totalWidth / 2) + offset), (int) (y + thickness + 4), 7, 7);
                    org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
                    offset += 7;
                    if (absorption)
                    {
                        Fonts.doOneText(event.getContext(), absorptionText, x - (totalWidth / 2) + offset, y + thickness + 4, healthColor, FontModule.INSTANCE.textShadow.getValue());
                        offset += Fonts.getTextWidth(absorptionText);
                        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                        event.getContext().drawGuiTexture(RenderPipelines.GUI_TEXTURED, InGameHud.HeartType.ABSORBING.getTexture(false, false, false), (int) (x - (totalWidth / 2) + offset), (int) (y + thickness + 4), 7, 7);
                        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
                    }
                }
                break;
        }
    }

    private Color getHealthColor()
    {
        float f2 = mc.player.getMaxHealth();
        float f3 = Math.max(0.0f, Math.min(mc.player.getHealth(), f2) / f2);
        return new Color(Color.HSBtoRGB(f3 / 3.0f, 1.0f, 1.0f) | 0xFF000000);
    }

    @Override
    public String getDescription()
    {
        return "HealthBar: Draws a health bars";
    }
}
