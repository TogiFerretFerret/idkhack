package sh.idktheflag.idk.impl.features.modules.movement;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.block.Blocks;

public class IceSpeed extends Module {


    public IceSpeed()
    {
        super("IceSpeed", Category.Movement);
    }

    Value<Number> slipperiness = new ValueBuilder<Number>()
            .withDescriptor("Slipperiness")
            .withValue(0.4)
            .withRange(0.2, 1.5)
            .register(this);

    @Override
    public void onDisable()
    {
        super.onDisable();
        if (NullUtils.nullCheck()) return;

        if (Blocks.ICE == null || Blocks.PACKED_ICE == null || Blocks.FROSTED_ICE == null)
        {
            return;
        }
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck())
        {
            return;
        }

        if (Blocks.ICE == null || Blocks.PACKED_ICE == null || Blocks.FROSTED_ICE == null)
        {
            return;
        }
        Blocks.ICE.slipperiness = this.slipperiness.getValue().floatValue();
        Blocks.PACKED_ICE.slipperiness = this.slipperiness.getValue().floatValue();
        Blocks.FROSTED_ICE.slipperiness = this.slipperiness.getValue().floatValue();

    }

    @Override
    public String getHudInfo()
    {
        return MathUtil.round(slipperiness.getValue().doubleValue(), 1) + "";
    }

    @Override
    public String getDescription()
    {
        return "IceSpeed: Slip N' Slide at rapid speeds";
    }
}

