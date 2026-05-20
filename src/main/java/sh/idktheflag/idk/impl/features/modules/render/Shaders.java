package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.mixin.accessor.IGameRenderer;
import net.minecraft.util.Identifier;

public class Shaders extends Module {
    public static Shaders INSTANCE;

    public Shaders() {
        super("Shaders", Category.Render);
        INSTANCE = this;
    }

    public Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Default")
            .withModes("Default", "Image", "Rainbow")
            .withAction(v -> {
                if (this.isEnabled()) apply();
            })
            .register(this);

    @Override
    public void onEnable() {
        if (mc.world == null) return;
        apply();
    }

    @Override
    public void onDisable() {
        if (mc.world == null) return;
        ((IGameRenderer) mc.gameRenderer).invokeSetPostProcessor(null);
    }

    public void apply() {
        String m = mode.getValue().toLowerCase();
        Identifier id = Identifier.of("kami", "shaders/post/" + m + ".json");
        ((IGameRenderer) mc.gameRenderer).invokeSetPostProcessor(id);
    }

    @Override
    public String getDescription() {
        return "Shaders: Post-processing effects";
    }
}
