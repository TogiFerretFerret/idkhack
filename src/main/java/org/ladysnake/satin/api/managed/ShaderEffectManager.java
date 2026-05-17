package org.ladysnake.satin.api.managed;

import net.minecraft.util.Identifier;
import java.util.function.Consumer;

public final class ShaderEffectManager {
    private static final ShaderEffectManager INSTANCE = new ShaderEffectManager();
    public static ShaderEffectManager getInstance() { return INSTANCE; }
    public ManagedShaderEffect manage(Identifier id) { return null; }
    public ManagedShaderEffect manage(Identifier id, Consumer<?> callback) { return null; }
}
