package sh.idktheflag.idkhack.impl.features.hud;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idkhack.api.gui.font.Fonts;
import sh.idktheflag.idkhack.api.utils.math.MathUtil;
import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import sh.idktheflag.idkhack.api.utils.color.IdkColor;
import sh.idktheflag.idkhack.api.utils.render.ScaledResolution;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.features.modules.client.HudColors;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import java.util.List;
import net.minecraft.util.math.MathHelper;
import sh.idktheflag.idkhack.api.feature.hud.HudComponent;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.impl.gui.ClickGui;

import java.awt.*;


public class ArmorHud extends HudComponent
{

    public ArmorHud()
    {
        super("ArmorHud");
        immovable = true;
    }

    Value<Boolean> percentIcon = new ValueBuilder<Boolean>()
            .withDescriptor("Percent")
            .withValue(false)
            .register(this);
    Value<Boolean> small = new ValueBuilder<Boolean>()
            .withDescriptor("Small")
            .withValue(false)
            .register(this);
    Value<Boolean> triColor = new ValueBuilder<Boolean>()
            .withDescriptor("Tri Color")
            .withValue(false)
            .register(this);
    public Value<IdkColor> armorColorA = new ValueBuilder<IdkColor>()
            .withDescriptor("High Color")
            .withValue(new IdkColor(0, 255, 255, 255))
            .withParent(triColor)
            .withParentEnabled(true)
            .register(this);
    public Value<IdkColor> armorColorB = new ValueBuilder<IdkColor>()
            .withDescriptor("Middle Color")
            .withValue(new IdkColor(0, 0, 255, 255))
            .withParent(triColor)
            .withParentEnabled(true)
            .register(this);
    public Value<IdkColor> armorColorC = new ValueBuilder<IdkColor>()
            .withDescriptor("Low Color")
            .withValue(new IdkColor(87, 8, 97))
            .withParent(triColor)
            .withParentEnabled(true)
            .register(this);

    @SubscribeEvent
    @Override
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);

        if (NullUtils.nullCheck() || renderCheck(event)) return;

        renderArmorHUD(event.getContext(), true);
    }



    public void renderArmorHUD(DrawContext context, boolean percent)
    {
        ScaledResolution resolution = new ScaledResolution(mc);
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (mc.player.isSubmergedIn(FluidTags.WATER) ? 10 : 0);
        // TODO: port to 1.21.11 - PlayerInventory.armor removed, use equipment slots
        List<ItemStack> armorItems = List.of(
            mc.player.getEquippedStack(EquipmentSlot.FEET),
            mc.player.getEquippedStack(EquipmentSlot.LEGS),
            mc.player.getEquippedStack(EquipmentSlot.CHEST),
            mc.player.getEquippedStack(EquipmentSlot.HEAD)
        );
        for (ItemStack is : armorItems)
        {
            iteration++;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;

            context.drawItem(is, x, y);
            context.drawStackOverlay(mc.textRenderer, is, x, y);
            String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            Fonts.doOneText(context, s, (x + 19 - 2 - ClickGui.CONTEXT.getRenderer().getTextWidth(s)), (y + 9), HudColors.getTextColor(y + 9), true);
            if (percent)
            {
                float green = ((float) is.getMaxDamage() - (float) is.getDamage()) / (float) is.getMaxDamage();
                float red = 1 - green;
                int dmg = 100 - (int) (red * 100);

                if(small.getValue())
                {
                    context.getMatrices().pushMatrix();
                    context.getMatrices().scale(0.625f, 0.625f);
                    Fonts.doOneText(
                            context,
                            dmg + (percentIcon.getValue() ? "%" : ""),
                            ((x + 6) * 1.6f) - (ClickGui.CONTEXT.getRenderer().getTextWidth((dmg + (percentIcon.getValue() ? "%" : ""))) / 2.0f) * 0.6f,
                            (y * 1.6f) - 11,
                            getArmorColor(dmg, y + 9),
                            true);
                    context.getMatrices().popMatrix();
                }else{
                    Fonts.doOneText(
                            context,
                            dmg + (percentIcon.getValue() ? "%" : ""),
                            (x + 8 - ClickGui.CONTEXT.getRenderer().getTextWidth((dmg + (percentIcon.getValue() ? "%" : ""))) / 2.0f),
                            y - 9,
                            getArmorColor(dmg, y + 9),
                            true);
                }
            }
        }
    }

    public Color getArmorColor(int dmg, int y)
    {
        if (triColor.getValue())
        {
            if (dmg < 50)
            {
                return ColorUtil.interpolate((float) MathHelper.clamp(MathUtil.normalize(dmg, 1, 50), 0, 1), armorColorB.getValue().getColor(), armorColorC.getValue().getColor());
            } else
            {
                return ColorUtil.interpolate((float) MathHelper.clamp(MathUtil.normalize(dmg, 50, 100), 0, 1), armorColorA.getValue().getColor(), armorColorB.getValue().getColor());
            }
        } else
        {
            return HudColors.getTextColor(y);
        }
    }

    @Override
    public String getDescription()
    {
        return "ArmorHud: Renders your Armor on screen";
    }
}
