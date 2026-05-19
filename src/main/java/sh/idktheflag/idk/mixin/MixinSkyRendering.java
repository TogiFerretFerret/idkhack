package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.impl.features.modules.render.CustomSky;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.state.SkyRenderState;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRendering.class)
public class MixinSkyRendering {

    @Inject(method = "updateRenderState", at = @At("RETURN"))
    private void hookUpdateRenderState(ClientWorld world, float tickDelta, Camera camera, SkyRenderState state, CallbackInfo ci) {
        if (CustomSky.INSTANCE != null && CustomSky.INSTANCE.isEnabled()) {
            state.skyColor = CustomSky.INSTANCE.fogColor.getValue().getColor().getRGB();
        }
    }
}
