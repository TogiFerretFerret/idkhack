package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class TimeChanger extends Module {

    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Day")
            .withModes("Day", "Noon", "Sunset", "Night", "Custom")
            .register(this);

    Value<Number> customTime = new ValueBuilder<Number>()
            .withDescriptor("Custom Time")
            .withValue(6000)
            .withRange(0, 23999)
            .withPageParent(mode)
            .withPage("Custom")
            .register(this);

    public TimeChanger() {
        super("TimeChanger", Category.Render);
    }

    private long getTargetTime() {
        return switch (mode.getValue()) {
            case "Day"    -> 1000L;
            case "Noon"   -> 6000L;
            case "Sunset" -> 12000L;
            case "Night"  -> 18000L;
            default       -> customTime.getValue().longValue();
        };
    }

    @SubscribeEvent
    public void onTick(TickEvent.AfterClientTickEvent event) {
        if (NullUtils.nullCheck()) return;
        long t = getTargetTime();
        ((ClientWorld) mc.world).setTime(t, t, false);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            event.setCancelled(true);
        }
    }

    @Override
    public String getHudInfo() {
        return mode.getValue();
    }

    @Override
    public String getDescription() {
        return "TimeChanger: lock the client-side time of day";
    }
}
