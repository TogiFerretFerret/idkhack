package sh.idktheflag.idk.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.util.math.MatrixStack;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.utils.render.world.buffers.RenderBuffers;

import com.mojang.blaze3d.buffers.GpuBufferSlice;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer
{
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderHead(ObjectAllocator objectAllocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, Matrix4f projectionMatrix2, GpuBufferSlice gpuBufferSlice, Vector4f vector4f, boolean bl, CallbackInfo ci) {
        RenderUtil.matrix4f.set(modelViewMatrix);
        RenderBuffers.preRender();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderTail(ObjectAllocator objectAllocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, Matrix4f projectionMatrix2, GpuBufferSlice gpuBufferSlice, Vector4f vector4f, boolean bl, CallbackInfo ci) {
        RenderUtil.matrix4f.set(modelViewMatrix);
        MatrixStack stack = new MatrixStack();
        RenderUtil.setMatrices(stack.peek(), modelViewMatrix);
        new RenderWorldEvent(stack, tickCounter.getTickProgress(false)).post();
        RenderBuffers.process();
        RenderBuffers.postRender();
    }
}
