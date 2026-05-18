package sh.idktheflag.idk.api.utils.render.animation.type;

import lombok.Setter;
import sh.idktheflag.idk.api.utils.color.ColorUtil;
import sh.idktheflag.idk.api.utils.render.animation.Animation;
import sh.idktheflag.idk.api.utils.render.animation.Easing;

import java.awt.*;


@Setter
public class DynamicColorAnimation extends Animation {

    Color normalColor;
    Color lastColor;

    public DynamicColorAnimation(Color normalColor, Easing easing, long animationTime)
    {
        super(easing, animationTime, true);
        this.normalColor = normalColor;
        this.lastColor = normalColor;
    }
    public void gotoColor(Color color){
        if(normalColor.equals(color) ) return;


        this.lastColor = this.normalColor;
        this.normalColor = color;
        this.time = System.currentTimeMillis();
    }

    public Color getColor()
    {
        return ColorUtil.interpolate(getScaledTime(), normalColor, lastColor);
    }
}

