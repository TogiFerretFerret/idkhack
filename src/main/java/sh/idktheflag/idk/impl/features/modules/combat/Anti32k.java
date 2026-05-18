package sh.idktheflag.idk.impl.features.modules.combat;


import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.FriendManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.utils.players.Utils32k;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;


import java.util.HashSet;
import java.util.Set;

public class Anti32k extends Module {
    public static Anti32k INSTANCE;
    Value<String> page = new ValueBuilder<String>()
            .withDescriptor("Page")
            .withValue("Survival")
            .withModes("Survival")
            .register(this);

    Value<Boolean> mainhandTotems = new ValueBuilder<Boolean>()
            .withDescriptor("Mainhand Totems")
            .withValue(true)
            .withPageParent(page)
            .withPage("Survival")
            .register(this);

    Value<Number> survivalRange = new ValueBuilder<Number>()
            .withDescriptor("Range", "survRange")
            .withValue(13)
            .withRange(6, 20)
            .withPageParent(page)
            .withPage("Survival")
            .register(this);

    Value<Boolean> log = new ValueBuilder<Boolean>()
            .withDescriptor("Log")
            .withValue(true)
            .withPageParent(page)
            .withPage("Survival")
            .register(this);

    public Anti32k() {
        super("Anti32k", Category.Combat);
        INSTANCE = this;
    }


    // must add your own auto32k position to this list to prevent self block
    public final Set<BlockPos> visitedPositions = new HashSet<>();

    private boolean didPreRotate;

    PlayerEntity opp;

    @Override
    public void onEnable() {
        super.onEnable();

        if (NullUtils.nullCheck()) return;
    }

    @SubscribeEvent
    public void onUpdate(final TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) return;

        doSurvival();
    }

    public void doSurvival() {
        if (isSafe()) return;

        int totemSlot = InventoryUtils.getHotbarItemSlot(Items.TOTEM_OF_UNDYING);

        if (totemSlot == -1 && !mc.player.getOffHandStack().getItem().equals(Items.TOTEM_OF_UNDYING) && log.getValue()) {
            mc.player.networkHandler.getConnection().disconnect(Text.of(Formatting.AQUA + "[Anti32k]" + Formatting.RED + "\n" + opp.getName().getString() + Formatting.RESET + " tried to 32k you! \n Logged with " + PlayerUtils.getColoredHealth(mc.player, false) + Formatting.RESET + " health remaining!"));
            return;
        }
        if (totemSlot == -1) return;

        if (mainhandTotems.getValue())
            InventoryUtils.switchToSlot(totemSlot);
    }


    public boolean isSafe() {

        if (Utils32k.checkSharpness(mc.player.getMainHandStack())) {
            return true;
        }

        for (PlayerEntity e : mc.world.getPlayers()) {
            if (e.equals(mc.player)) continue;

            if (FriendManager.INSTANCE.isFriend(e)) continue;

            if ((mc.player.distanceTo(e) > survivalRange.getValue().floatValue())) continue;

            if (!Utils32k.checkSharpness(e.getMainHandStack())) continue;

            opp = e;
            return false;
        }
        return true;
    }




    @Override
    public String getDescription() {
        return "Anti32k: Mainhands a totem or logs if u have none if someone has a 32k in range";
    }
}
