package sh.idktheflag.idk.impl.features.modules.movement.timer;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.render.RenderTimer;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

public class Timer extends Module {
    public Timer()
    {
        super("Timer", Category.Movement);
    }

    Value<Number> timerAmount = new ValueBuilder<Number>()
            .withDescriptor("Timer Amount")
            .withValue(5)
            .withRange(0.1, 10)
            .register(this);

    float oldTickLength = 1.0F;
    int boostTime;
    int boostSpaceTime;

    @Override
    public void onEnable()
    {
        super.onEnable();

        if (NullUtils.nullCheck()) return;

        oldTickLength = RenderTimer.getTickLength();
        boostTime = 0;
        doLoop = false;
        boostSpaceTime = 0;
    }

    boolean doLoop;

    @Override
    public void onDisable()
    {
        super.onDisable();

        if (NullUtils.nullCheck()) return;

        if (mc.getRenderTickCounter()  == null) return;

        RenderTimer.setTickLength(oldTickLength);

    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;


        if (mc.getRenderTickCounter() == null) return;

        RenderTimer.setTickLength(timerAmount.getValue().floatValue());
    }

    @Override
    public String getHudInfo()
    {
        return MathUtil.round(timerAmount.getValue().floatValue(), 1) + "";
    }

    @Override
    public String getDescription()
    {
        return "Timer: Decreases/Increases the amount of ticks in a second";
    }
}
