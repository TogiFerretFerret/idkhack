package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.player.PopEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.ColorUtil;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.utils.render.world.RenderType;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PopChams extends Module {
    public static PopChams INSTANCE;

    public PopChams() {
        super("PopChams", Category.Render);
        INSTANCE = this;
    }

    private final List<PopData> pops = new ArrayList<>();

    Value<Number> duration = new ValueBuilder<Number>()
            .withDescriptor("Duration")
            .withValue(1500)
            .withRange(100, 5000)
            .register(this);

    Value<IdkColor> color = new ValueBuilder<IdkColor>()
            .withDescriptor("Color")
            .withValue(new IdkColor(255, 255, 255, 100))
            .register(this);

    @SubscribeEvent
    public void onPop(PopEvent.TotemPopEvent event) {
        if (NullUtils.nullCheck()) return;
        if (event.getEntity() == mc.player) return;

        pops.add(new PopData(event.getEntity().getBoundingBox(), System.currentTimeMillis()));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        pops.removeIf(pop -> System.currentTimeMillis() - pop.time > duration.getValue().longValue());

        for (PopData pop : pops) {
            long age = System.currentTimeMillis() - pop.time;
            float alpha = 1.0f - (float) age / duration.getValue().floatValue();
            Color c = ColorUtil.newAlpha(color.getValue().getColor(), (int) (color.getValue().getColor().getAlpha() * alpha));

            RenderUtil.renderBox(RenderType.FILL, pop.bb, c, c);
            RenderUtil.renderBox(RenderType.LINES, pop.bb, c, c);
        }
    }

    private record PopData(Box bb, long time) {}

    @Override
    public String getDescription() {
        return "PopChams: Totem pop visualization";
    }
}
