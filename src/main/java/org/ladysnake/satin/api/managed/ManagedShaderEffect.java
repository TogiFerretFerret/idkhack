package org.ladysnake.satin.api.managed;

import net.minecraft.client.gl.PostEffectProcessor;

public interface ManagedShaderEffect {
    PostEffectProcessor getPostEffectProcessor();
    void render(float tickDelta);
    ManagedUniform findUniform1f(String name);
    ManagedUniform findUniform4f(String name);
    boolean isInitialized();
}
