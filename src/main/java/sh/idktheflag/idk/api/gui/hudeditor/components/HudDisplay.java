package sh.idktheflag.idk.api.gui.hudeditor.components;

import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.feature.hud.HudComponent;
import sh.idktheflag.idk.api.gui.hudeditor.HudEditorGUI;
import sh.idktheflag.idk.api.gui.component.IComponent;
import sh.idktheflag.idk.api.gui.context.Context;
import sh.idktheflag.idk.api.gui.flow.Flow;
import sh.idktheflag.idk.api.gui.flow.impl.LinearFlow;
import sh.idktheflag.idk.api.gui.helpers.IDraggable;
import sh.idktheflag.idk.api.gui.helpers.MouseHelper;
import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.gui.render.IRenderer;
import sh.idktheflag.idk.impl.gui.ClickGui;

import java.awt.*;

public class HudDisplay implements IComponent, IDraggable {

    String name;
    Rect dims;
    Rect displayRect;
    boolean dragging;
    HudComponent hudComponent;
    int dragX;
    int dragY;
    Flow flow;

    public HudDisplay(Rect dims, HudComponent hudComponent)
    {
        this.name = hudComponent.getName();
        Rect newdims = dims;
        newdims.setX(hudComponent.xPos.getValue().intValue());
        newdims.setY(hudComponent.yPos.getValue().intValue());
        this.dims = newdims;
        this.displayRect = dims;
        this.hudComponent = hudComponent;
        dragging = false;
        flow = new LinearFlow(new Rect(0, 0, 0, 0), 1);
    }

    public String getName()
    {
        return name;
    }


    @Override
    public void draw(Context context, MouseHelper mouse)
    {
        getDims().setWidth(hudComponent.getWidth());
        getDims().setHeight(hudComponent.getHeight());
        getDims().setX(hudComponent.xPos.getValue().intValue());
        getDims().setY(hudComponent.yPos.getValue().intValue());
        if (hudComponent.isEnabled() && !hudComponent.immovable)
        {

            if (dragging)
            {
                dragEditor(getDims(), dragX, dragY, mouse, hudComponent);
            }

            getDims().setX(getDims().getX());
            flow.getDims().setY(getDims().getY());
            flow.positionComponents(context);
            displayRect = new Rect(getDims().getX(), getDims().getY(), getDims().getWidth(), getDims().getHeight());

            HudEditorGUI.CONTEXT.getRenderer().renderRect(getDims().fixRect(), getDims().collideWithMouse(mouse) ? new Color(255, 255, 255, 100) : new Color(0, 0, 0, 100), getDims().collideWithMouse(mouse) ? new Color(255, 255, 255, 100) : new Color(0, 0, 0, 100), IRenderer.RectMode.Fill, HudEditorGUI.CONTEXT);

            hudComponent.xPos.setValue(getDims().getX());
            hudComponent.yPos.setValue(getDims().getY());


            hudComponent.draw(new RenderGameOverlayEvent.Text(context.getDrawContext(), null));

            // render the name
            if (getDims().collideWithMouse(mouse))
            {

                HudEditorGUI.CONTEXT.getRenderer().renderText(context.getDrawContext(), hudComponent.getName(), mouse.getX() + 10, mouse.getY(), Color.WHITE, ClickGui.CONTEXT.getColorScheme().doesTextShadow());

            }
        }
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button)
    {
        if (displayRect.collideWithMouse(mouse) && !hudComponent.immovable)
        {
            if (button == 0)
            {
                if (isDraggable())
                {
                    dragging = true;
                    dragX = mouse.getX() - getDims().getX();
                    dragY = mouse.getY() - getDims().getY();
                }
            }
        }

        if (!hudComponent.immovable)
        {
            flow.click(context, mouse, button);
        }
    }

    @Override
    public void release(Context context, MouseHelper mouse, int state)
    {
        dragging = false;
        if (!hudComponent.immovable)
        {
            flow.release(context, mouse, state);
        }
    }

    @Override
    public void key(Context context, int key, char keycode)
    {
        if (!hudComponent.immovable)
        {
            flow.key(context, key, keycode);
        }
    }

    public void charTyped(Context context, char character)
    {

    }

    @Override
    public int getLevel()
    {
        return 0;
    }

    @Override
    public Rect getDims()
    {
        return dims;
    }

    public Flow getFlow()
    {
        return flow;
    }

    public void setFlow(Flow flow)
    {
        this.flow = flow;
    }

    @Override
    public boolean isDraggable()
    {
        return true;
    }

    @Override
    public boolean isActive()
    {
        return true;
    }
}
