package sh.idktheflag.idk.mixin;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import sh.idktheflag.idk.api.utils.render.RenderTimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.RenderTickCounter;

@Mixin(RenderTickCounter.Dynamic.class)
public class MixinRenderTimer {

    @Shadow
    private float dynamicDeltaTicks;
    @Shadow
    private long lastTimeMillis;
    @Shadow
    private float tickTime;
    @Shadow
    private FloatUnaryOperator targetMillisPerTick;

    @Inject(method = "beginRenderTick(JZ)I", at = @At("HEAD"), cancellable = true)
    private void beginRenderTick(long timeMillis, boolean bl, CallbackInfoReturnable<Integer> ci)
    {
        if (RenderTimer.getTickLength() == 1.0f) return;

        float frameDuration = (float) ((timeMillis - this.lastTimeMillis) / this.targetMillisPerTick.apply(this.tickTime)) * RenderTimer.getTickLength();
        this.lastTimeMillis = timeMillis;
        this.dynamicDeltaTicks += frameDuration;
        int i = (int) this.dynamicDeltaTicks;
        this.dynamicDeltaTicks -= (float) i;

        ci.setReturnValue(i);
    }

}
