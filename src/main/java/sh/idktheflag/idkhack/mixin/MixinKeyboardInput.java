package sh.idktheflag.idkhack.mixin;

import sh.idktheflag.idkhack.api.event.events.key.InputEvent;
import sh.idktheflag.idkhack.api.event.events.move.SneakEvent;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class MixinKeyboardInput extends Input {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void hookTick$Pre(CallbackInfo info)
    {
        InputEvent event = new InputEvent(this);
        event.post();
        if (event.isCancelled())
        {
            info.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void hookTick$Post(CallbackInfo ci)
    {
        InputEvent event = new InputEvent(this);
        event.post();
    }
}
