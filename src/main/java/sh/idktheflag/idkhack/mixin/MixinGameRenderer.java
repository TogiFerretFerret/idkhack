package sh.idktheflag.idkhack.mixin;

import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.utils.ducks.IVec3d;
import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import sh.idktheflag.idkhack.impl.features.modules.misc.NoEntityTrace;
import sh.idktheflag.idkhack.impl.features.modules.render.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer implements IMinecraft
{
    @Shadow
    public abstract void updateCrosshairTarget(float tickDelta);

    @Inject(method = "renderWorld", at = @At("TAIL"))
    public void hookRenderWorld(RenderTickCounter tickCounter, CallbackInfo ci)
    {
        new RenderWorldEvent(new net.minecraft.client.util.math.MatrixStack(), tickCounter.getTickProgress(false)).post();
    }

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
            ((IVec3d) new Vec3d(cameraE.getX(), cameraE.getY(), cameraE.getZ())).set(freecam.pos.x, freecam.pos.y - cameraE.getEyeHeight(cameraE.getPose()), freecam.pos.z);
            cameraE.lastX = freecam.prevPos.x;
            cameraE.lastY = freecam.prevPos.y - cameraE.getEyeHeight(cameraE.getPose());
            cameraE.lastZ = freecam.prevPos.z;
            cameraE.setYaw(freecam.yaw);
            cameraE.setPitch(freecam.pitch);

            freecamSet = true;
            updateCrosshairTarget(tickDelta);
            freecamSet = false;

            ((IVec3d) new Vec3d(cameraE.getX(), cameraE.getY(), cameraE.getZ())).set(x, y, z);
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
