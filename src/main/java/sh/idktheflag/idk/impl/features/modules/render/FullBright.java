package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.event.events.render.LightmapGammaEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;


public class FullBright extends Module {


    public FullBright()
    {
        super("FullBright", Category.Render);
    }

    public Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Gamma")
            .withModes("Gamma", "Potion")
            .register(this);


    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;


        if (mode.getValue().equals("Potion") && !mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION))
        {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0));
        }
    }


    @Override
    public void onDisable()
    {
        super.onDisable();
        if (NullUtils.nullCheck()) return;

        if (mode.getValue().equals("Potion"))
        {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    @SubscribeEvent
    public void onLightmapGamma(LightmapGammaEvent event)
    {
        if (mode.getValue().equals("Gamma"))
        {
            event.setCancelled(true);
            event.setGamma(0xffffffff);
        }
    }


    @Override
    public void onEnable()
    {
        super.onEnable();
    }

    @Override
    public String getDescription()
    {
        return "FullBright: Brights up the entire world";
    }

}
