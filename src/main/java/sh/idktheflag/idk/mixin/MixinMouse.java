package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.api.event.events.key.MouseEvent;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.features.modules.render.Freecam;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse implements IMinecraft {

    @Mutable @Shadow private double cursorDeltaX;
    @Mutable @Shadow private double cursorDeltaY;

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

    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
    private void hookUpdateMouse(double elapsed, CallbackInfo ci)
    {
        if (!Freecam.INSTANCE.isEnabled()) return;
        if (mc.currentScreen != null) return;

        Freecam.INSTANCE.changeLookDirection(cursorDeltaX * 0.15, cursorDeltaY * 0.15);
        cursorDeltaX = 0;
        cursorDeltaY = 0;
        ci.cancel();
    }
}
