package sh.idktheflag.idk.impl.features.modules.client;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.network.DiscordPresence;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

public class RPC extends Module {

    public static RPC INSTANCE;
    public Value<String> text = new ValueBuilder<String>()
            .withDescriptor("Text")
            .withValue("I love cats so flipping much")
            .register(this);
    public Value<String> image = new ValueBuilder<String>()
            .withDescriptor("Images")
            .withValue("Animals")
            .withModes("Animals", "idkIcon", "Grails")
            .register(this);
    public RPC() {
        super("RPC", Category.Client);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }

    @Override
    public String getDescription() {
        return "RPC: Rep idk on discord";
    }
}
