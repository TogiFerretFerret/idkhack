package me.skitttyy.kami.api.management.shaders;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import me.skitttyy.kami.api.wrapper.IMinecraft;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

import java.awt.*;


@Getter
public class ShaderManager implements IMinecraft
{
    public static ShaderManager INSTANCE;
    // TODO: port to 1.21.11 - OutlineVertexConsumerProvider constructor changed
    // private final OutlineVertexConsumerProvider vertexConsumerProvider = new OutlineVertexConsumerProvider(VertexConsumerProvider.immediate(new BufferAllocator(256)));

    public ManagedShaderEffect defaultShaderEffect;
    public ManagedShaderEffect imageShaderEffect;
    public ManagedShaderEffect rainbowShaderEffect;

    public ShaderManager()
    {
        // TODO: port to 1.21.11 - RenderPhase.Target, RenderPhase.TextureBase, RenderPhase.Texture,
        // RenderPhase.OUTLINE_PROGRAM, RenderPhase.DISABLE_CULLING, RenderPhase.ALWAYS_DEPTH_TEST,
        // RenderLayer.MultiPhaseParameters, RenderLayer.OutlineMode have all been removed.
        // The new system uses RenderSetup and RenderPipeline instead.
    }

    public void reloadShaders()
    {
        // TODO: port to 1.21.11 - ShaderFramebuffer and Framebuffer API completely changed.
        // Framebuffer is now abstract, fbo field removed, beginWrite/endWrite/clear/draw removed.
        reloadShadersInternal();
    }

    public void reloadShadersInternal()
    {
        // TODO: port to 1.21.11 - ShaderFramebuffer needs rewrite for new Framebuffer API
        defaultShaderEffect = ShaderEffectManager.getInstance().manage(Identifier.of("kami", "shaders/post/default.json"));
        imageShaderEffect = ShaderEffectManager.getInstance().manage(Identifier.of("kami", "shaders/post/image.json"));
        rainbowShaderEffect = ShaderEffectManager.getInstance().manage(Identifier.of("kami", "shaders/post/rainbow.json"));
    }

    // TODO: port to 1.21.11 - applyShader needs complete rewrite.
    // Framebuffer API completely changed: now abstract, no fbo field, no beginWrite/endWrite/clear/draw.
    // GlStateManager.SrcFactor/DstFactor removed, RenderSystem.blendFuncSeparate removed.
    // PostEffectProcessor API changed, IPostEffectProcessor mixin needs update.
    public void applyShader(ManagedShaderEffect shaderEffect, Runnable setup, Runnable runnable)
    {
        // Stub - needs rewrite for 1.21.11 framebuffer/shader pipeline
        runnable.run();
        setup.run();
    }

    public VertexConsumerProvider createVertexConsumers(VertexConsumerProvider parent, Color color)
    {
        // TODO: port to 1.21.11 - RenderLayer.MultiPhase, IRenderLayerMultiPhase, IRenderLayerMultiPhaseParameters,
        // IRenderPhaseTextureBase, RenderPhase.TextureBase have all been removed.
        // The outline/shader vertex consumer creation needs to be rewritten for the new pipeline.
        return parent;
    }

}
