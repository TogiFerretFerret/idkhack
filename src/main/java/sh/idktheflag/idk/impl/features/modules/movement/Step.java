package sh.idktheflag.idk.impl.features.modules.movement;

import com.google.common.collect.Streams;
import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.event.events.move.TravelEvent;
import sh.idktheflag.idk.api.event.events.render.FrameEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.PacketManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.utils.players.rotation.RotationUtils;
import sh.idktheflag.idk.api.utils.render.RenderTimer;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.api.utils.Timer;
import sh.idktheflag.idk.impl.features.modules.combat.AutoFeetPlace;
import sh.idktheflag.idk.impl.features.modules.player.MiddleClick;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Step extends Module
{

    Value<Number> stepHeight = new ValueBuilder<Number>()
            .withDescriptor("Step Height")
            .withValue(2.1)
            .withRange(0.1, 7)
            .register(this);

    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Vanilla")
            .withModes("Vanilla", "Normal")
            .register(this);
    Value<Boolean> timer = new ValueBuilder<Boolean>()
            .withDescriptor("Use Timer")
            .withValue(false)
            .withPageParent(mode)
            .withPage("Normal")
            .register(this);
    Value<Boolean> extra = new ValueBuilder<Boolean>()
            .withDescriptor("Extra")
            .withValue(false)
            .withPageParent(mode)
            .withPage("Normal")
            .register(this);
    Value<Boolean> alternate = new ValueBuilder<Boolean>()
            .withDescriptor("Alternate")
            .withValue(false)
            .withPageParent(mode)
            .withPage("Normal")
            .register(this);
    public static Step INSTANCE;
    Timer stepTimer = new Timer();
    private boolean cancelTimer;
    Timer grimTimer = new Timer();


    public Step()
    {
        super("Step", Category.Movement);
        INSTANCE = this;
    }

    boolean runPhysics = false;


    @Override
    public void onDisable()
    {
        super.onDisable();
        if (NullUtils.nullCheck()) return;

        setStepHeight(0.6f);
        if (timer.getValue())
        {
            RenderTimer.setTickLength(1.0f);
        }
        stepTimer.setDelay(50);
        grimTimer.setDelay(200);
    }


    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.MovementTickEvent.Pre event)
    {

        if (NullUtils.nullCheck()) return;


        if (mode.getValue().equals("Normal"))
        {
            double height = mc.player.getY() - mc.player.lastY;
            if (height <= 0.5 || height > stepHeight.getValue().doubleValue())
            {
                return;
            }

            if (alternate.getValue() && height <= 1.5f)
                return;

            //
            final double[] offs = getStepOffsets(height);
            if (timer.getValue())
            {
                RenderTimer.setTickLength(height > 1.0 ? 0.15f : 0.35f);
                cancelTimer = true;
            }
            for (double off : offs)
            {
                PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.lastX, mc.player.lastY + off, mc.player.lastZ, false, mc.player.horizontalCollision));
            }
            stepTimer.resetDelay();
            if (AutoFeetPlace.INSTANCE.isEnabled())
            {
                AutoFeetPlace.INSTANCE.setEnabled(false);
            }
        }

    }


    public void doNCPStep(double[] dir, boolean forceStep)
    {
        boolean twofive = false;
        boolean two = false;
        boolean onefive = false;
        boolean one = false;


        if (Streams.stream(mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(dir[0], 21, dir[1]))).toList().isEmpty() && !Streams.stream(mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(dir[0], 2.4, dir[1]))).toList().isEmpty())
        {
            two = true;
        }

        if (Streams.stream(mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(dir[0], 2.1, dir[1]))).toList().isEmpty() && !Streams.stream(mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(dir[0], 1.9, dir[1]))).toList().isEmpty())
        {
            two = true;
        }
        if (Streams.stream(mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(dir[0], 1.6, dir[1]))).toList().isEmpty() && !Streams.stream(mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(dir[0], 1.4, dir[1]))).toList().isEmpty())
        {
            onefive = true;
        }

        if (Streams.stream(mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(dir[0], 1.0, dir[1]))).toList().isEmpty() && !Streams.stream(mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(dir[0], 0.6, dir[1]))).toList().isEmpty())
        {
            one = true;
        }
        if (mc.player.horizontalCollision && ((mc.player.input.getMovementInput().y != 0.0f || mc.player.input.getMovementInput().x != 0.0f) || forceStep) && mc.player.isOnGround())
        {
            if (one && stepHeight.getValue().doubleValue() >= 1.0)
            {
                final double[] oneOffset = getStepOffsets(1.0);

                for (double v : oneOffset)
                {
                    PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.lastX, mc.player.lastY + v, mc.player.lastZ, false, mc.player.horizontalCollision));
                }
                mc.player.setPosition(mc.player.getX(), mc.player.getY() + 1.0, mc.player.getZ());
            }
            if (onefive && stepHeight.getValue().doubleValue() >= 1.5)
            {
                final double[] oneFiveOffset = getStepOffsets(1.5f);
                for (double v : oneFiveOffset)
                {
                    PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.lastX, mc.player.lastY + v, mc.player.lastZ, false, mc.player.horizontalCollision));
                }
                mc.player.setPosition(mc.player.getX(), mc.player.getY() + 1.5, mc.player.getZ());
            }
            if (two && stepHeight.getValue().doubleValue() >= 2.0)
            {
                final double[] twoOffset = getStepOffsets(2.0);
                for (double v : twoOffset)
                {
                    PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.lastX, mc.player.lastY + v, mc.player.lastZ, false, mc.player.horizontalCollision));
                }
                mc.player.setPosition(mc.player.getX(), mc.player.getY() + 2.0, mc.player.getZ());
            }
            if (twofive && stepHeight.getValue().doubleValue() >= 2.5)
            {
                final double[] twoFiveOffset = getStepOffsets(2.5);
                for (double v : twoFiveOffset)
                {
                    PacketManager.INSTANCE.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.lastX, mc.player.lastY + v, mc.player.lastZ, false, mc.player.horizontalCollision));
                }
                mc.player.setPosition(mc.player.getX(), mc.player.getY() + 2.5f, mc.player.getZ());
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;


        stepTimer.setDelay(50);
        if (mc.player.isTouchingWater() || mc.player.isInLava() || mc.player.isGliding())
        {
            if (timer.getValue())
                RenderTimer.setTickLength(1.0f);
            setStepHeight(0.6f);
            return;
        }
        if (cancelTimer && mc.player.isOnGround())
        {
            if (timer.getValue())
                RenderTimer.setTickLength(1.0f);
            cancelTimer = false;
        }
        if (mc.player.isOnGround() && stepTimer.isPassed())
        {
            setStepHeight(stepHeight.getValue().floatValue());
        } else
        {
            setStepHeight(0.6f);
        }
    }

    private double[] getStepOffsets(double stepHeight)
    {
        double[] offsets = new double[0];
        if (extra.getValue())
        {
            if (stepHeight > 1.1661)
            {
                offsets = new double[]{0.42, 0.7532, 1.001, 1.1661, stepHeight};
            } else if (stepHeight > 1.015)
            {
                offsets = new double[]{0.42, 0.7532, 1.001, stepHeight};
            } else if (stepHeight > 0.6)
            {
                offsets = new double[]{0.42 * stepHeight, 0.7532 * stepHeight, stepHeight};
            }
            return offsets;
        }
        if (stepHeight > 2.019)
        {
            offsets = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.919};
        } else if (stepHeight > 1.5)
        {
            offsets = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
        } else if (stepHeight > 1.015)
        {
            offsets = new double[]{0.42, 0.7532, 1.01, 1.093, 1.015};
        } else if (stepHeight > 0.6)
        {
            offsets = new double[]{0.42 * stepHeight, 0.7532 * stepHeight};
        }
        return offsets;
    }

    public static void setStepHeight(float height)
    {
        if (mc.player != null && mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT) != null) {
            mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT).setBaseValue(height);
        }
    }


    @Override
    public String getHudInfo()
    {
        return mode.getValue();
    }

    @Override
    public String getDescription()
    {
        return "Step: Steps up blocks faster/higher then you normally can";
    }
}
