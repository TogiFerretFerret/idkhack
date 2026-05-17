package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;

import java.awt.*;


public class WeatherEditor extends Module {
    public static WeatherEditor INSTANCE;
    public Value<String> weatherMode = new ValueBuilder<String>()
            .withDescriptor("Weather Mode")
            .withValue("Clear")
            .withModes("Clear", "Ash", "Rain", "Thunder", "Normal")
            .register(this);
    public Value<Boolean> colorize = new ValueBuilder<Boolean>()
            .withValue(false)
            .withDescriptor("Colorize")
            .register(this);

    public Value<Sn0wColor> lightningColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Lightning")
            .withValue(new Sn0wColor(0, 255, 255, 255))
            .withParentEnabled(true)
            .withParent(colorize)
            .register(this);
    public WeatherEditor()
    {
        super("WeatherEditor", Category.Render);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onTick(TickEvent.AfterClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;

        setWeather(weatherMode.getValue());
    }

    private void setWeather(String weather)
    {
        switch (weather)
        {
            case "Clear", "Ash" ->
            {
                mc.world.getLevelProperties().setRaining(false);
                mc.world.setRainGradient(0.0f);
                mc.world.setThunderGradient(0.0f);
            }
            case "Rain" ->
            {
                mc.world.getLevelProperties().setRaining(true);
                mc.world.setRainGradient(1.0f);
                mc.world.setThunderGradient(0.0f);
            }
            case "Thunder" ->
            {
                mc.world.getLevelProperties().setRaining(true);
                mc.world.setRainGradient(2.0f);
                mc.world.setThunderGradient(1.0f);
            }
        }
    }

    @SubscribeEvent
    public void onPacketInbound(PacketEvent.Receive event)
    {

        if(weatherMode.getValue().equals("Normal")) return;

        if (event.getPacket() instanceof GameStateChangeS2CPacket packet)
        {
            if (packet.getReason() == GameStateChangeS2CPacket.RAIN_STARTED || packet.getReason() == GameStateChangeS2CPacket.RAIN_STOPPED || packet.getReason() == GameStateChangeS2CPacket.RAIN_GRADIENT_CHANGED || packet.getReason() == GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED)
            {
                event.setCancelled(true);
            }
        }
    }


    @Override
    public String getHudInfo()
    {
        return weatherMode.getValue();
    }

    @Override
    public String getDescription()
    {
        return "WeatherEditor: change weather like god mcswag";
    }

}
