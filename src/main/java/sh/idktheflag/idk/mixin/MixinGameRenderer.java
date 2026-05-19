package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.impl.features.modules.render.*;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer implements IMinecraft
{
    @Shadow
    public abstract void updateCrosshairTarget(float tickDelta);

    @Inject(method = "tiltViewWhenHurt", at = @At(value = "HEAD"), cancellable = true)
    private void hookTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci)
    {
        if (NoRender.INSTANCE.noHurtCam.getValue() && NoRender.INSTANCE.isEnabled())
        {
            ci.cancel();
        }
    }

    private boolean freecamSet = false;

    @Inject(method = "updateCrosshairTarget", at = @At("HEAD"), cancellable = true)
    private void updateTargetedEntityInvoke(float tickDelta, CallbackInfo info)
    {
        Freecam freecam = Freecam.INSTANCE;

        if ((freecam.isEnabled()) && mc.getCameraEntity() != null && !freecamSet)
        {
            info.cancel();
            Entity cameraE = mc.getCameraEntity();

            double x = cameraE.getX();
            double y = cameraE.getY();
            double z = cameraE.getZ();
            double prevX = cameraE.lastX;
            double prevY = cameraE.lastY;
            double prevZ = cameraE.lastZ;
            float yaw = cameraE.getYaw();
            float pitch = cameraE.getPitch();
            float prevYaw = cameraE.lastYaw;
            float prevPitch = cameraE.lastPitch;
            double eyeOffset = cameraE.getEyeHeight(cameraE.getPose());
            cameraE.setPosition(freecam.pos.x, freecam.pos.y - eyeOffset, freecam.pos.z);
            cameraE.lastX = freecam.prevPos.x;
            cameraE.lastY = freecam.prevPos.y - eyeOffset;
            cameraE.lastZ = freecam.prevPos.z;
            cameraE.setYaw(freecam.yaw);
            cameraE.setPitch(freecam.pitch);

            freecamSet = true;
            updateCrosshairTarget(tickDelta);
            freecamSet = false;

            cameraE.setPosition(x, y, z);
            cameraE.lastX = prevX;
            cameraE.lastY = prevY;
            cameraE.lastZ = prevZ;
            cameraE.setYaw(yaw);
            cameraE.setPitch(pitch);
            cameraE.lastYaw = prevYaw;
            cameraE.lastPitch = prevPitch;
        }
    }

    // TODO: 1.21.11 - getBasicProjectionMatrix hook removed (zoom/zoomX/zoomY fields removed)
    // TODO: 1.21.11 - getFov hook removed (lastFovMultiplier/renderingPanorama fields removed)

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void showFloatingItemHook(ItemStack floatingItem, CallbackInfo info)
    {
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.totem.getValue())
        {
            info.cancel();
        }
    }

    @Inject(method = "shouldRenderBlockOutline", at = @At(value = "HEAD"),
            cancellable = true)
    private void hookShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir)
    {
        if (BlockHighlight.INSTANCE.isEnabled())
        {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
