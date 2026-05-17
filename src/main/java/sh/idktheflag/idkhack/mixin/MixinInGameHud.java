package sh.idktheflag.idkhack.mixin;

import sh.idktheflag.idkhack.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idkhack.impl.features.modules.render.NoRender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(at = @At(value = "RETURN"), method = "renderMainHud")
    public void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci)
    {
        new RenderGameOverlayEvent.Text(context, tickCounter).post();
    }

    @Inject(method = "renderOverlay", at = @At(value = "HEAD"), cancellable = true)
    private void hookRenderOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci)
    {
        if (NoRender.INSTANCE.isEnabled())
        {
            String path = texture.getPath();
            if (path.contains("pumpkin"))
            {
                if (NoRender.INSTANCE.pumpkin.getValue())
                {
                    ci.cancel();
                }
            } else if (path.contains("powder_snow"))
            {
                if (NoRender.INSTANCE.frost.getValue())
                {
                    ci.cancel();
                }
            }
        }
    }


    @Inject(method = "renderStatusEffectOverlay", at = @At(value = "HEAD"), cancellable = true)
    private void hookRenderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci)
    {
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.potionGui.getValue())
        {
            ci.cancel();
        }
    }


}
