package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Nametags extends Module {
    public static Nametags INSTANCE;

    public Nametags() {
        super("Nametags", Category.Render);
        INSTANCE = this;
    }

    public Value<Boolean> armor = new ValueBuilder<Boolean>()
            .withDescriptor("Armor")
            .withValue(true)
            .register(this);

    public Value<Number> scale = new ValueBuilder<Number>()
            .withDescriptor("Scale")
            .withValue(1.0)
            .withRange(0.1, 5.0)
            .withPlaces(1)
            .register(this);

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (player.isSpectator()) continue;

            Vec3d renderPos = player.getLerpedPos(event.getTickDelta()).add(0, player.getHeight() + 0.5, 0);
            String text = player.getName().getString() + " " + PlayerUtils.getColoredHealth(player, true);
            
            RenderUtil.drawText(text, renderPos, scale.getValue().floatValue());
        }
    }

    @Override
    public String getDescription() {
        return "Nametags: Custom nametags for players";
    }
}
