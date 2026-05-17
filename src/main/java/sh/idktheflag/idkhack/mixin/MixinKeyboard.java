package sh.idktheflag.idkhack.mixin;

import sh.idktheflag.idkhack.api.event.events.key.CharTypeEvent;
import sh.idktheflag.idkhack.api.event.events.key.KeyboardEvent;
import sh.idktheflag.idkhack.api.gui.GUI;
import sh.idktheflag.idkhack.api.gui.hudeditor.HudEditorGUI;
import sh.idktheflag.idkhack.impl.IdkHackMod;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int action, KeyInput keyInput, CallbackInfo ci)
    {
        int key = keyInput.key();
        if (key != GLFW.GLFW_KEY_UNKNOWN)
        {
            KeyboardEvent event = new KeyboardEvent(false, key, action);
            event.post();
            if (event.isCancelled())
            {
                ci.cancel();
            }

        }
    }

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onChar", at = @At("HEAD"), cancellable = true)
    private void onChar(long window, CharInput charInput, CallbackInfo ci)
    {
        int codePoint = charInput.codepoint();
        if (window == this.client.getWindow().getHandle())
        {
            Screen screen = this.client.currentScreen;
            if (screen != null)
            {
                if (screen instanceof GUI || screen instanceof HudEditorGUI)
                {
                    if (Character.charCount(codePoint) == 1)
                    {
                        new CharTypeEvent((char) codePoint).post();
                        ci.cancel();
                    } else
                    {
                        char[] var6 = Character.toChars(codePoint);

                        for (char c : var6)
                        {
                            new CharTypeEvent(c).post();
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }

}
