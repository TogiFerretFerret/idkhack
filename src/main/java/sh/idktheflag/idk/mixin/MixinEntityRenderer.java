package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.impl.features.modules.render.Chams;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.lwjgl.opengl.GL11;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements IMinecraft
{
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderPre(EntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState, CallbackInfo ci) {
        if (Chams.INSTANCE != null && Chams.INSTANCE.isEnabled() && Chams.INSTANCE.throughWalls.getValue()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderPost(EntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState, CallbackInfo ci) {
        if (Chams.INSTANCE != null && Chams.INSTANCE.isEnabled() && Chams.INSTANCE.throughWalls.getValue()) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }
}
