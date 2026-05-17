package sh.idktheflag.idkhack.mixin;

import sh.idktheflag.idkhack.api.event.events.key.MouseEvent;
import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse implements IMinecraft {

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    public void mouseEvent(long window, MouseInput mouseInput, int action, CallbackInfo ci)
    {
        if (window == mc.getWindow().getHandle())
        {
            int button = mouseInput.button();
            MouseEvent event = new MouseEvent(button, MouseEvent.Type.of(action));
            event.post();
            if (event.isCancelled())
            {
                ci.cancel();
            }
        }
    }

    // TODO: 1.21.11 - updateMouse signature changed, Freecam look redirect disabled
}
