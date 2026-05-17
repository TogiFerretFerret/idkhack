package me.skitttyy.kami.api.management.shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;

// TODO: port to 1.21.11 - Framebuffer is now abstract with different constructor/resize signatures
// Only use this framebuffer if you are rendering shaders
public class ShaderFramebuffer extends Framebuffer
{
    public ShaderFramebuffer(int width, int height)
    {
        super("shader_framebuffer", false);
        RenderSystem.assertOnRenderThread();
        resize(width, height);
        // setClearColor removed in 1.21.11
    }
}
