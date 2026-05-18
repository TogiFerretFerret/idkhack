package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.player.PopEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.world.RenderType;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
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

    Value<Sn0wColor> color = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Color")
            .withValue(new Sn0wColor(255, 255, 255, 100))
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
