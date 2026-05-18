package sh.idktheflag.idk.impl.features.modules.movement;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.utils.render.RenderTimer;
import sh.idktheflag.idk.api.utils.world.HoleUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.util.Formatting;

public class TickBase extends Module {

    public TickBase()
    {
        super("TickBase", Category.Movement);
        INSTANCE = this;
    }


    public static TickBase INSTANCE;

    Value<Number> timerAmount = new ValueBuilder<Number>()
            .withDescriptor("Timer Amount")
            .withValue(5)
            .withRange(1, 5)
            .register(this);
    Value<Number> boostSpacing = new ValueBuilder<Number>()
            .withDescriptor("Boost Spacing")
            .withValue(10)
            .withRange(1, 30)
            .register(this);
    Value<Number> BoostTime = new ValueBuilder<Number>()
            .withDescriptor("Boost Time")
            .withValue(25)
            .withRange(10, 100)
            .register(this);
    Value<Number> ChargeSpeed = new ValueBuilder<Number>()
            .withDescriptor("Charge Speed")
            .withValue(25)
            .withRange(10, 100)
            .register(this);
    Value<String> Mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Spacing")
            .withModes("Disable", "Charge", "Spacing")
            .withAction(set ->
            {
                boostSpacing.setActive(set.getValue().equals("Spacing"));
                ChargeSpeed.setActive(set.getValue().equals("Charge"));
            })
            .register(this);

    float oldTickLength = 1.0F;
    int boostTime;
    int boostSpaceTime;

    @Override
    public void onEnable()
    {
        super.onEnable();

        boostTime = 0;
        doLoop = false;
        boostSpaceTime = 0;

        if (NullUtils.nullCheck()) return;

        oldTickLength = RenderTimer.getTickLength();

    }

    HoleUtils.Hole pk;
    boolean doLoop;

    @Override
    public void onDisable()
    {
        super.onDisable();

        if (NullUtils.nullCheck()) return;

        if (mc.getRenderTickCounter() == null) return;

        RenderTimer.setTickLength(oldTickLength);

    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;


        if (mc.getRenderTickCounter() == null)
        {
            return;
        }


        if (!Mode.getValue().equals("Charge"))
        {

            if (!doLoop)
                boostTime++;

            if (doLoop)
            {
                boostSpaceTime++;
            }

            if (boostSpaceTime > boostSpacing.getValue().intValue() && doLoop)
            {
                doLoop = false;
                boostTime = 0;
            }
            if (boostTime < BoostTime.getValue().intValue() && !doLoop)
            {
                RenderTimer.setTickLength(timerAmount.getValue().floatValue());
            } else if (!doLoop)
            {
                RenderTimer.setTickLength(oldTickLength);
                doLoop = true;
                boostSpaceTime = 0;
                if (Mode.getValue().equals("Disable"))
                {
                    this.toggle();
                }
            }
        } else
        {
//            if (Holesnap.INSTANCE.isEnabled())
//            {
//                boostTime = 0;
//                return;
//            }
//            if (PacketFly.INSTANCE.isEnabled())
//            {
//                boostTime = 0;
//                return;
//            }

            if (mc.player.getVelocity().x == 0 && mc.player.getVelocity().z == 0)
            {
                RenderTimer.setTickLength(oldTickLength);
                if (boostTime < BoostTime.getValue().intValue())
                    boostTime++;
            } else
            {
                if (PlayerUtils.isActuallyMoving())
                {
                    if (boostTime > 0)
                    {
                        RenderTimer.setTickLength(timerAmount.getValue().floatValue());
                        boostTime--;
                    } else
                    {
                        RenderTimer.setTickLength(oldTickLength);
                    }
                } else
                {
                    RenderTimer.setTickLength(oldTickLength);
                }
            }
        }
    }

    @Override
    public String getHudInfo()
    {
        return getTime();
    }

    public String getTime()
    {
        if (!doLoop)
        {
            return getTimeColor(boostTime) + "" + boostTime;
        } else
        {
            return getBoostColor(boostSpaceTime) + "" + boostSpaceTime;
        }
    }

    public Formatting getTimeColor(double time)
    {
        return Formatting.WHITE;
    }

    public Formatting getBoostColor(double time)
    {
        return Formatting.WHITE;

    }

    @Override
    public String getDescription()
    {
        return "TickBase: Bases timer off of various things";
    }
}
