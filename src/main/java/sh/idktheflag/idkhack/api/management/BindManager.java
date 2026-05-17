package sh.idktheflag.idkhack.api.management;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.key.InputEvent;
import sh.idktheflag.idkhack.api.event.events.key.KeyboardEvent;
import sh.idktheflag.idkhack.api.event.events.key.MouseEvent;
import sh.idktheflag.idkhack.api.feature.Feature;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import sh.idktheflag.idkhack.impl.IdkHackMod;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import sh.idktheflag.idkhack.api.binds.IBindable;

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
