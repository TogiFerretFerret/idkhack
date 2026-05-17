package sh.idktheflag.idkhack.api.utils.render.animation.type;

import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import sh.idktheflag.idkhack.api.utils.render.animation.Animation;
import sh.idktheflag.idkhack.api.utils.render.animation.Easing;

import java.awt.*;

public class ColorAnimation extends Animation {

    Color normalColor;
    Color activeColor;

    public ColorAnimation(Color normalColor, Color activeColor, Easing easing, long animationTime, boolean state)
    {
        super(easing, animationTime, state);
        this.normalColor = normalColor;
        this.activeColor = activeColor;
    }

    public Color getColor()
    {
        return ColorUtil.interpolate(getScaledTime(), normalColor, activeColor);
    }
}
