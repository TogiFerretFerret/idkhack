package sh.idktheflag.idk.api.management.shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.SimpleFramebuffer;

public class ShaderFramebuffer extends SimpleFramebuffer
{
    public ShaderFramebuffer(int width, int height)
    {
        super("shader_framebuffer", width, height, false);
        RenderSystem.assertOnRenderThread();
    }
}
