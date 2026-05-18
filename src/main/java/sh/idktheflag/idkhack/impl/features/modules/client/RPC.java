package sh.idktheflag.idkhack.impl.features.modules.client;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.network.DiscordPresence;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

public class RPC extends Module {

    public static RPC INSTANCE;
    public Value<String> text = new ValueBuilder<String>()
            .withDescriptor("Text")
            .withValue("I love cats so flipping much")
            .register(this);
    public Value<String> image = new ValueBuilder<String>()
            .withDescriptor("Images")
            .withValue("Animals")
            .withModes("Animals", "idkhackIcon", "Grails")
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
        return "RPC: Rep idkhack on discord";
    }
}
