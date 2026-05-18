package sh.idktheflag.idk.impl.features.modules.client.gui;

import sh.idktheflag.idk.api.gui.widget.impl.*;
import sh.idktheflag.idk.api.utils.StringUtils;
import sh.idktheflag.idk.impl.gui.ClickGui;
import net.minecraft.client.gui.DrawContext;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.gui.hudeditor.HudEditorGUI;
import sh.idktheflag.idk.api.gui.context.Context;
import sh.idktheflag.idk.api.gui.helpers.MouseHelper;
import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.gui.render.IRenderer;
import sh.idktheflag.idk.api.gui.theme.IColorScheme;
import sh.idktheflag.idk.api.gui.theme.IMetrics;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.impl.gui.components.module.FeatureButton;

import java.awt.*;

public class HudEditorModule extends Module implements IColorScheme, IMetrics, IRenderer {

    public static HudEditorModule INSTANCE;

    public HudEditorModule()
    {
        super("HudEditor", Category.Client);
        INSTANCE = this;
    }

    @Override
    public void onEnable()
    {
        if (NullUtils.nullCheck()) return;

        super.onEnable();
        HudEditorGUI.INSTANCE.enterGui(this, this, this);
        setEnabled(false);
    }

    @Override
    public void renderBackground(Context context)
    {
        renderRect(
                new Rect(
                        0,
                        0,
                        context.getScaledResolution().getScaledWidth(),
                        context.getScaledResolution().getScaledHeight()
                ),
                IdkGui.INSTANCE.gradientTop.getValue().getColor(),
                IdkGui.INSTANCE.gradentBottom.getValue().getColor(),
                RectMode.Fill,
                context
        );

    }

    @Override
    public void renderLast(Context context)
    {
        IdkGui.INSTANCE.renderLast(context);
    }

    @Override
    public void renderFrameTitle(Context context, Rect rect, MouseHelper mouse, String title, boolean open)
    {
        IdkGui.INSTANCE.renderFrameTitle(context, rect, mouse, title, open);
    }

    @Override
    public void renderFrameOutline(Context context, Rect rect, MouseHelper mouse)
    {
        IdkGui.INSTANCE.renderFrameOutline(context, rect, mouse);
    }

    @Override
    public void renderFrame(Context context, Rect rect, MouseHelper mouse)
    {
        IdkGui.INSTANCE.renderFrame(context, rect, mouse);
    }

    @Override
    public void renderBooleanWidget(BooleanWidget widget, Context context, Rect rect, MouseHelper mouse)
    {
        IdkGui.INSTANCE.renderBooleanWidget(widget, context, rect, mouse);
    }

    @Override
    public void renderBindWidget(BindWidget widget, Context context, Rect rect, MouseHelper mouse)
    {
        IdkGui.INSTANCE.renderBindWidget(widget, context, rect, mouse);
    }


    @Override
    public void renderFeatureButton(FeatureButton widget, Context context, Rect rect, MouseHelper mouse)
    {
        IdkGui.INSTANCE.renderFeatureButton(widget, context, rect, mouse);
    }

    @Override
    public void renderComboBox(ComboBoxWidget widget, Context context, Rect rect, MouseHelper mouseHelper)
    {
        IdkGui.INSTANCE.renderComboBox(widget, context, rect, mouseHelper);
    }

    @Override
    public void renderSliderWidget(SliderWidget widget, Context context, Rect rect, Rect sliderRect, MouseHelper mouse)
    {
        IdkGui.INSTANCE.renderSliderWidget(widget, context, rect, sliderRect, mouse);
    }

    @Override
    public float getTextWidthFloat(String text)
    {
        return IdkGui.INSTANCE.getTextWidthFloat(text);
    }

    @Override
    public void renderColorWidget(ColorWidget widget, Context context, boolean open, Rect headerRect, Rect dims, Rect container, Rect alphaSlider, Rect hueSlider, Rect colorSquare)
    {
        IdkGui.INSTANCE.renderColorWidget(widget, context, open, headerRect, dims, container, alphaSlider, hueSlider, colorSquare);
    }

    @Override
    public void renderStringWidget(TextEntryWidget widget, Context context, Rect rect, MouseHelper mouse)
    {
        IdkGui.INSTANCE.renderStringWidget(widget, context, rect, mouse);
    }

    @Override
    public int getTextWidth(String text)
    {
        return mc.textRenderer.getWidth(text);
    }

    @Override
    public int getTextHeight(String text)
    {
        return mc.textRenderer.fontHeight;
    }

    @Override
    public void renderText(DrawContext context, String text, float x, float y, Color color, boolean shadow)
    {
        IdkGui.INSTANCE.renderText(context, text, x, y, color, shadow);
    }

    @Override
    public void renderRect(Rect rect, Color inputTop, Color inputBottom, RectMode mode, Context context)
    {
        IdkGui.INSTANCE.renderRect(rect, inputTop, inputBottom, mode, context);
    }


    @Override
    public void scissorRect(Rect dims)
    {
        IdkGui.INSTANCE.scissorRect(dims);
    }

    @Override
    public Color getMainColor(int pos)
    {
        return IdkGui.INSTANCE.getMainColor(pos);
    }

    @Override
    public Color getOutlineColor()
    {
        return IdkGui.INSTANCE.getOutlineColor();
    }

    @Override
    public Color getButtonColor()
    {
        return IdkGui.INSTANCE.getButtonColor();
    }

    @Override
    public Color getBackgroundColor()
    {
        return IdkGui.INSTANCE.getBackgroundColor();
    }

    @Override
    public Color getSecondaryBackgroundColor()
    {
        return IdkGui.INSTANCE.getSecondaryBackgroundColor();
    }

    @Override
    public Color getTertiaryBackgroundColor()
    {
        return IdkGui.INSTANCE.getTertiaryBackgroundColor();
    }

    @Override
    public Color getTextColor()
    {
        return IdkGui.INSTANCE.getTextColor();
    }

    @Override
    public Color getTextColorHighlight()
    {
        return IdkGui.INSTANCE.getTextColorHighlight();
    }

    @Override
    public Color getTextColorActive()
    {
        return IdkGui.INSTANCE.getTextColorActive();
    }

    @Override
    public boolean doesTextShadow()
    {
        return IdkGui.INSTANCE.doesTextShadow();
    }

    @Override
    public int getSpacing()
    {
        return IdkGui.INSTANCE.getSpacing();
    }

    @Override
    public int getBetweenSpacing()
    {
        return IdkGui.INSTANCE.getBetweenSpacing();
    }

    @Override
    public int getSettingSpacing()
    {
        return IdkGui.INSTANCE.getSettingSpacing();
    }

    @Override
    public int getFrameWidth()
    {
        return IdkGui.INSTANCE.getFrameWidth();
    }

    @Override
    public int getButtonHeight()
    {
        return IdkGui.INSTANCE.getButtonHeight();
    }

    @Override
    public int getFrameHeight()
    {
        return IdkGui.INSTANCE.getFrameHeight();
    }


    // component renderer stuffs

    public void registerGUI()
    {
        HudEditorGUI.INSTANCE.updateGUI(this, this, this);
    }

    @Override
    public String getDescription()
    {
        return "HudEditor: edit the various hud elements";
    }
}