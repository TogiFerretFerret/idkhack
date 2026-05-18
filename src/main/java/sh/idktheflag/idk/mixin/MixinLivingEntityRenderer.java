package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.impl.features.modules.render.Chams;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer implements IMinecraft
{
    @Inject(method = "getMixColor", at = @At("RETURN"), cancellable = true)
    private void onGetMixColor(LivingEntityRenderState state, CallbackInfoReturnable<Integer> cir) {
        if (Chams.INSTANCE == null || !Chams.INSTANCE.isEnabled()) return;

        boolean isPlayer = state.entityType == EntityType.PLAYER;
        boolean isCrystal = state.entityType == EntityType.END_CRYSTAL;

        if ((isPlayer && Chams.INSTANCE.players.getValue()) || (isCrystal && Chams.INSTANCE.crystals.getValue())) {
            cir.setReturnValue(Chams.INSTANCE.color.getValue().getColor().getRGB());
        }
    }

    @Inject(method = "getRenderLayer", at = @At("RETURN"), cancellable = true)
    private void onGetRenderLayer(LivingEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline, CallbackInfoReturnable<RenderLayer> cir) {
        if (Chams.INSTANCE == null || !Chams.INSTANCE.isEnabled()) return;

        boolean isPlayer = state.entityType == EntityType.PLAYER;
        boolean isCrystal = state.entityType == EntityType.END_CRYSTAL;

        if ((isPlayer && Chams.INSTANCE.players.getValue()) || (isCrystal && Chams.INSTANCE.crystals.getValue())) {
            if (Chams.INSTANCE.throughWalls.getValue()) {
                cir.setReturnValue(RenderLayers.entityTranslucent(getTexture(state), true));
            }
        }
    }

    @Shadow
    public abstract net.minecraft.util.Identifier getTexture(LivingEntityRenderState state);
}
