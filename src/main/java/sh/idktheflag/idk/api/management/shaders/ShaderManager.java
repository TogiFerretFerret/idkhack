package sh.idktheflag.idk.api.management.shaders;

import lombok.Getter;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.mixin.accessor.IMinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import java.awt.*;
import java.util.Set;

@Getter
public class ShaderManager implements IMinecraft
{
    public static ShaderManager INSTANCE;

    private PostEffectProcessor defaultShaderEffect;
    private PostEffectProcessor imageShaderEffect;
    private PostEffectProcessor rainbowShaderEffect;

    public ShaderManager()
    {
        INSTANCE = this;
    }

    public void reloadShaders()
    {
        reloadShadersInternal();
    }

    public void reloadShadersInternal()
    {
        var loader = ((IMinecraftClient) mc).getShaderLoader();
        if (loader == null) return;

        try {
            defaultShaderEffect = loader.loadPostEffect(Identifier.of("kami", "shaders/post/default.json"), Set.of());
            imageShaderEffect = loader.loadPostEffect(Identifier.of("kami", "shaders/post/image.json"), Set.of());
            rainbowShaderEffect = loader.loadPostEffect(Identifier.of("kami", "shaders/post/rainbow.json"), Set.of());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void applyShader(PostEffectProcessor shaderEffect, Runnable setup, Runnable runnable)
    {
        runnable.run();
        setup.run();
    }

    public VertexConsumerProvider createVertexConsumers(VertexConsumerProvider parent, Color color)
    {
        return parent;
    }
}
