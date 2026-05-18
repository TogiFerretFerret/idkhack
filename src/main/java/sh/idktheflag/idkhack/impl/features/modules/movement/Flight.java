package sh.idktheflag.idkhack.impl.features.modules.movement;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.LivingEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.move.MoveEvent;
import sh.idktheflag.idkhack.api.event.events.move.TravelEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.PacketManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.Timer;
import sh.idktheflag.idkhack.api.utils.players.InventoryUtils;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import sh.idktheflag.idkhack.api.utils.players.rotation.RotationUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.features.modules.player.MiddleClick;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class Flight extends Module
{
    public static Flight INSTANCE;

    public Flight()
    {
        super("Flight", Category.Movement);
        INSTANCE = this;
    }

    @Override
    public void onDisable()
    {
        if (mc.player != null)
        {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().allowFlying = false;
        }
    }

    public Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Creative")
            .withModes("Creative", "Grim")
            .withAction(val -> handlePage(val.getValue()))
            .register(this);
    Value<Number> horizontalSpeed = new ValueBuilder<Number>()
            .withDescriptor("Horizontal")
            .withValue(3f)
            .withRange(1, 5)
            .register(this);
    Value<Number> verticalSpeed = new ValueBuilder<Number>()
            .withDescriptor("Vertical")
            .withValue(3f)
            .withRange(1, 5)
            .register(this);
    Value<Boolean> antiKick = new ValueBuilder<Boolean>()
            .withDescriptor("AntiKick")
            .withValue(false)
            .register(this);

    //grim
    Value<Boolean> pitch = new ValueBuilder<Boolean>()
            .withDescriptor("Pitch")
            .withValue(false)
            .register(this);
    Value<Boolean> firework = new ValueBuilder<Boolean>()
            .withDescriptor("Firework")
            .withValue(false)
            .register(this);
    Timer antiKickTimer = new Timer(3800);
    Timer fireworkDelay = new Timer(400);

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent.Pre event)
    {
        if (NullUtils.nullCheck()) return;

        if (mode.getValue().equals("Creative"))
        {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().allowFlying = true;
        }

        if (mode.getValue().equals("Grim"))
            RotationUtils.setRotation(new float[]{PlayerUtils.getMoveYaw(mc.player.getYaw()), ElytraFly.INSTANCE.getControlPitch()}, 99);

    }

    @SubscribeEvent
    public void onPlayerMove(MoveEvent event)
    {
        switch (mode.getValue())
        {
            case "Creative":
                event.motionY(0.0);
                if (antiKickTimer.isPassed() && antiKick.getValue())
                {

                    event.motionY(-0.04);
                    antiKickTimer.resetDelay();
                } else
                {
                    if (mc.options.jumpKey.isPressed())
                    {
                        event.motionY(verticalSpeed.getValue().floatValue());
                    } else if (mc.options.sneakKey.isPressed())
                    {
                        event.motionY(-verticalSpeed.getValue().floatValue());
                    }
                }
                float speed = horizontalSpeed.getValue().floatValue();
                float forward = mc.player.input.getMovementInput().y;
                float strafe = mc.player.input.getMovementInput().x;
                float yaw = mc.player.getYaw();
                if (forward == 0.0f && strafe == 0.0f)
                {
                    event.motionX(0.0);
                    event.motionZ(0.0);
                    return;
                }
                double rx = Math.cos(Math.toRadians(yaw + 90.0f));
                double rz = Math.sin(Math.toRadians(yaw + 90.0f));
                event.motionX((forward * speed * rx) + (strafe * speed * rz));
                event.motionZ((forward * speed * rz) - (strafe * speed * rx));
                break;
            case "Grim":
                if (ElytraFly.INSTANCE.boostControl.getValue())
                    ElytraFly.INSTANCE.doBoost(event);

                break;
        }
    }


    @SubscribeEvent
    public void onTravel(TravelEvent.Pre event)
    {
        if (NullUtils.nullCheck()) return;


        switch (mode.getValue())
        {
            case "Grim":


                int slot = InventoryUtils.getInventoryItemSlot(Items.ELYTRA);


                if (!PlayerUtils.isElytraEquipped())
                    if (slot == -1) return;


                if (!PlayerUtils.isMoving() && !(mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed()) && !(mc.options.sneakKey.isPressed() && !mc.options.jumpKey.isPressed()))
                {
                    event.setCancelled(true);
                    return;
                }
                if (!mc.player.isGliding())
                {


                    boolean swapBack = false;
                    if (!PlayerUtils.isElytraEquipped())
                    {
                        InventoryUtils.swapArmor(2, slot);
                        swapBack = true;
                    }

                    PacketManager.INSTANCE.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                    mc.player.startGliding();


                    if (firework.getValue())
                    {
                        if (!PlayerUtils.isBoostedByFirework() && fireworkDelay.isPassed() && !mc.player.isOnGround())
                        {
                            PlayerUtils.doFirework();
                            fireworkDelay.resetDelay();
                        }

                    }
                    if (MiddleClick.INSTANCE.fireworkSchedule)
                    {
                        MiddleClick.INSTANCE.doFirework();
                        MiddleClick.INSTANCE.fireworkSchedule = false;
                    }

                    if (swapBack)
                        InventoryUtils.swapArmor(2, slot);
                }


                if (mc.player.isOnGround())
                    PlayerUtils.clientJump();
                break;
            case "Creative":
                break;
        }

    }

    @SubscribeEvent
    public void onActionJump(LivingEvent.Jump event)
    {
        if (NullUtils.nullCheck()) return;


        if (mode.getValue().equals("Grim"))
            event.setCancelled(true);
    }


    public void handlePage(String page)
    {
        //Creative
        horizontalSpeed.setActive(page.equals("Creative"));
        verticalSpeed.setActive(page.equals("Creative"));
        antiKick.setActive(page.equals("Creative"));

        //Grim
        pitch.setActive(page.equals("Grim"));
        firework.setActive(page.equals("Grim"));

    }

    public static boolean isGrimFlying()
    {
        if (!INSTANCE.isEnabled()) return false;


        int slot = InventoryUtils.getInventoryItemSlot(Items.ELYTRA);
        if (slot == -1) return false;

        return INSTANCE.mode.getValue().equals("Grim");
    }

    @Override
    public String getHudInfo()
    {
        return mode.getValue();
    }

    @Override
    public String getDescription()
    {
        return "Flight: I just flew 100 blocks thanks to idkhackgod.cc! (or now catogod.cc)";
    }
}