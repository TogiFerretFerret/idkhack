package sh.idktheflag.idkhack.api.gui.component;

import sh.idktheflag.idkhack.api.gui.context.Context;
import sh.idktheflag.idkhack.api.gui.helpers.MouseHelper;
import sh.idktheflag.idkhack.api.gui.helpers.Rect;

public interface IComponent {

    void draw(Context context, MouseHelper mouse);
    void click(Context context, MouseHelper mouse, int button);
    void release(Context context, MouseHelper mouse, int state);
    void key(Context context, int key, char character);
    void charTyped(Context context, char character);

    int getLevel();
    Rect getDims();
    boolean isDraggable();
    boolean isActive();

}
