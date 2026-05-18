package sh.idktheflag.idk.api.management;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.key.InputEvent;
import sh.idktheflag.idk.api.event.events.key.KeyboardEvent;
import sh.idktheflag.idk.api.event.events.key.MouseEvent;
import sh.idktheflag.idk.api.feature.Feature;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.IdkHackMod;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import sh.idktheflag.idk.api.binds.IBindable;

import java.util.ArrayList;
import java.util.List;

public class BindManager implements IMinecraft {

    public static BindManager INSTANCE;

    List<IBindable> bindables;

    public BindManager()
    {
        bindables = new ArrayList<>();
        IdkHackMod.EVENT_BUS.register(this);
    }

    public List<IBindable> getBindables()
    {
        return bindables;
    }

    public void setBindables(List<IBindable> bindables)
    {
        this.bindables = bindables;
    }

    @SubscribeEvent
    public void onKey(KeyboardEvent event)
    {
        if (mc.currentScreen == null)
            for (IBindable bindable : getBindables())
            {
                if (bindable.getKey() == event.getKey() && event.getAction() == GLFW.GLFW_PRESS)
                {
                    bindable.onKey();
                }
            }
    }


    @SubscribeEvent
    public void onKey(MouseEvent event)
    {
        if (!event.getType().equals(MouseEvent.Type.CLICK)) return;

        for (Feature feature : FeatureManager.INSTANCE.getFeatures())
        {
            if (feature instanceof Module)
            {
                Module module = (Module) feature;
                if (module.getBind().getIsMouse())
                {
                    if (module.getBind().getKey() == event.getButton())
                    {
                        module.toggle();
                    }
                }
            }
        }
    }

}
