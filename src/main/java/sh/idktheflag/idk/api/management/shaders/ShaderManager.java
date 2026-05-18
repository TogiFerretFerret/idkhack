package sh.idktheflag.idk.api.management.shaders;

import lombok.Getter;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import net.minecraft.client.render.*;
import java.awt.*;

@Getter
public class ShaderManager implements IMinecraft
{
    public static ShaderManager INSTANCE;

    public Object defaultShaderEffect;
    public Object imageShaderEffect;
    public Object rainbowShaderEffect;

    public ShaderManager()
    {
    }

    public void reloadShaders()
    {
        reloadShadersInternal();
    }

    public void reloadShadersInternal()
    {
    }

    public void applyShader(Object shaderEffect, Runnable setup, Runnable runnable)
    {
        runnable.run();
        setup.run();
    }

    public VertexConsumerProvider createVertexConsumers(VertexConsumerProvider parent, Color color)
    {
        return parent;
    }
}
