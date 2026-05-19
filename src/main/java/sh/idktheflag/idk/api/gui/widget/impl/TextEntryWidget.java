package sh.idktheflag.idk.api.gui.widget.impl;

import sh.idktheflag.idk.api.gui.component.IComponent;
import sh.idktheflag.idk.api.gui.context.Context;
import sh.idktheflag.idk.api.gui.helpers.MouseHelper;
import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.utils.StringUtils;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import net.minecraft.SharedConstants;
import net.minecraft.client.util.InputUtil;
import sh.idktheflag.idk.api.gui.widget.IWidget;
import net.minecraft.client.util.SelectionManager;
import org.lwjgl.glfw.GLFW;

import static sh.idktheflag.idk.api.wrapper.IMinecraft.mc;

public class TextEntryWidget implements IWidget<String>, IComponent {

    Rect dims;
    String value;
    public boolean typing = false;

    public TextEntryWidget(Rect dims, String value)
    {
        this.dims = dims;
        this.value = value;
    }

    @Override
    public void draw(Context context, MouseHelper mouse)
    {
        getDims().setHeight(context.getMetrics().getButtonHeight());
        context.getRenderer().renderStringWidget(this, context, getDims(), mouse);
    }


    @Override
    public void click(Context context, MouseHelper mouse, int button)
    {
        if (getDims().collideWithMouse(mouse) && button == 0)
        {
            typing = !typing;
        }else{
            typing = false;
        }
    }

    @Override
    public void release(Context context, MouseHelper mouse, int state)
    {

    }

    @Override
    public void key(Context context, int key, char character)
    {
        if (!typing) return;

        switch (key)
        {
            case GLFW.GLFW_KEY_V ->
            {
                if (InputUtil.isKeyPressed(mc.getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL))
                {
                    setValue(getValue() + SelectionManager.getClipboard(mc));
                }
            }
            case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER ->
            {
                typing = false;
            }
            case GLFW.GLFW_KEY_BACKSPACE ->
            {
                setValue(StringUtils.removeLastChar(getValue()));
            }
        }
    }

    @Override
    public void charTyped(Context context, char character)
    {
        if (typing)
        {
            setValue(getValue() + character);
        }
    }

    @Override
    public int getLevel()
    {
        return 3;
    }

    @Override
    public Rect getDims()
    {
        return dims;
    }

    @Override
    public boolean isDraggable()
    {
        return false;
    }

    @Override
    public boolean isActive()
    {
        return true;
    }

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String getTitle()
    {
        return "";
    }

    @Override
    public void setTitle(String title)
    {

    }

    @Override
    public Rect getDisplayDims()
    {
        return getDims();
    }
}
