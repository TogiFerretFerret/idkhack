package sh.idktheflag.idkhack.api.management.notification.types.component;

import lombok.Getter;
import lombok.Setter;
import sh.idktheflag.idkhack.api.gui.font.Fonts;
import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import sh.idktheflag.idkhack.api.utils.render.ScaledResolution;
import sh.idktheflag.idkhack.api.utils.render.animation.Easing;
import sh.idktheflag.idkhack.api.utils.render.animation.type.ColorAnimation;
import sh.idktheflag.idkhack.api.utils.render.animation.type.DynamicAnimation;
import sh.idktheflag.idkhack.api.utils.render.animation.type.DynamicColorAnimation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

@Getter
@Setter
public class TopComponent {
    private String text;
    private DynamicColorAnimation animation;
    private DynamicAnimation verticalAnimation;
    boolean setIndex;
    private Color color;
    boolean visible;

    public TopComponent(String text, long animationSpeed, Color color)
    {
        this.text = text;
        this.color = color;
        visible = false;
        this.animation = new DynamicColorAnimation(ColorUtil.newAlpha(color, 0), Easing.CUBIC_IN_OUT, animationSpeed);
        animation.setState(true);
        setIndex = false;
        verticalAnimation = new DynamicAnimation(0, Easing.EXPO_IN_OUT, 200L);
        verticalAnimation.setStateHard(false);
    }


    public void draw(DrawContext context, int index, ScaledResolution resolution)
    {
        animation(index);
        if (animation.getColor().getAlpha() == 0)
        {
            setIndex = false;
            return;
        }
        float offset = resolution.getScaledWidth() / 2f + 1f - Fonts.getTextWidth(text) / 2f;
        float textY = 12 + (verticalAnimation.getAnimationValue());

        Fonts.doOneText(context, text, offset, textY, animation.getColor(), true);
    }

    public void animation(int index)
    {

        animation.gotoColor(visible ? color : ColorUtil.newAlpha(color, 0));

        if (!setIndex)
        {
            verticalAnimation.goal(index * Fonts.getTextHeight("AA"));
            verticalAnimation.setStateHard(true);
            setIndex = true;
            return;
        }

        verticalAnimation.goal(index * Fonts.getTextHeight("AA"));
    }

    public boolean isComplete()
    {
        return animation.getColor().getAlpha() == 0 && !visible;
    }

}
