package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.Timer;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ChestStealer extends Module {
    public static ChestStealer INSTANCE;
    Timer timer = new Timer();
    Value<Number> delay = new ValueBuilder<Number>()
            .withDescriptor("Delay")
            .withValue(0)
            .withRange(0, 1000)
            .withAction(set -> timer.setDelay(set.getValue().longValue()))
            .register(this);

    Value<Boolean> close = new ValueBuilder<Boolean>()
            .withDescriptor("Close")
            .withValue(false)
            .register(this);

    public ChestStealer()
    {
        super("ChestStealer", Category.Player);
        INSTANCE = this;
    }


    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event)
    {
        if (NullUtils.nullCheck()) return;


    }


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;


        if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler chest)
        {

            if (timer.isPassed())
            {
                for (int i = 0; i < chest.getInventory().size(); i++)
                {
                    Slot slot = chest.getSlot(i);
                    if (slot.hasStack())
                    {
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
                        timer.resetDelay();
                        break;
                    }
                }
            }
            if (isContainerEmpty(chest) && close.getValue())
                mc.player.closeHandledScreen();
        }else{

        }
    }

    private boolean isContainerEmpty(GenericContainerScreenHandler container)
    {
        for (int i = 0; i < (container.getInventory().size() == 90 ? 54 : 27); i++)
            if (container.getSlot(i).hasStack()) return false;
        return true;
    }


    @Override
    public String getDescription()
    {
        return "ChestStealer: steals from chests";
    }
}