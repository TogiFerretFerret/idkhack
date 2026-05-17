package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.FriendManager;
import sh.idktheflag.idkhack.api.management.PacketManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.players.InventoryUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.features.modules.misc.FakePlayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;

import java.time.Instant;
import java.util.BitSet;

public class AutoLog extends Module
{
    Value<Boolean> onRender = new ValueBuilder<Boolean>()
            .withDescriptor("Visual Range")
            .withValue(false)
            .register(this);
    Value<Boolean> illegalDisconnect = new ValueBuilder<Boolean>()
            .withDescriptor("Illegal")
            .withValue(false)
            .register(this);
    Value<Boolean> logOnHealth = new ValueBuilder<Boolean>()
            .withDescriptor("Lethal")
            .withValue(false)
            .register(this);
    Value<Number> logHealth = new ValueBuilder<Number>()
            .withDescriptor("Health")
            .withValue(15)
            .withRange(0, 36)
            .withPlaces(1)
            .withParent(logOnHealth)
            .withParentEnabled(true)
            .register(this);
    Value<Number> totemAmount = new ValueBuilder<Number>()
            .withDescriptor("Totems")
            .withValue(0)
            .withRange(0, 5)
            .withPlaces(0)
            .register(this);

    Value<Boolean> noTotems = new ValueBuilder<Boolean>()
            .withDescriptor("No Totems")
            .withValue(false)
            .register(this);

    public AutoLog()
    {
        super("AutoLog", Category.Player);
        // TODO 1.21.11: INSTANCE = this;
    }


    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent.Pre event)
    {
        if (NullUtils.nullCheck()) return;


        if (onRender.getValue())
        {
            // TODO 1.21.11: AbstractClientPlayerEntity player = mc.world.getPlayers().stream()
                    // TODO 1.21.11: .filter(p -> checkEnemy(p)).findFirst().orElse(null);
            if (player != null)
            {
                playerDisconnect("[AutoLog] %s came into render distance.", player.getName().getString());
                return;
            }
        }
        float health = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        int totems = InventoryUtils.getItemCount(Items.TOTEM_OF_UNDYING);
        boolean b2 = totems <= totemAmount.getValue().floatValue();
        if (health <= logHealth.getValue().floatValue())
        {
            if (!logOnHealth.getValue())
            {
                playerDisconnect("[AutoLog] logged out with %d hearts remaining.", (int) health);
                return;
            } else if (b2)
            {
                playerDisconnect("[AutoLog] logged out with %d totems and %d hearts remaining.", totems, (int) health);
                return;
            }
        }
        if (b2 && noTotems.getValue())
        {
            playerDisconnect("[AutoLog] logged out with %d totems remaining.", totems);
        }
    }


    private void playerDisconnect(String disconnectReason, Object... args)
    {
        if (illegalDisconnect.getValue())
        {
            PacketManager.INSTANCE.sendQuietPacket(new ChatMessageC2SPacket(
                    "§",
                    Instant.now(),
                    NetworkEncryptionUtils.SecureRandomUtil.nextLong(),
                    null,
                    null)); // TODO: port to 1.21.11 - LastSeenMessageList.Acknowledgment changed
            this.toggle();
            // TODO: 1.21.11 - return;
        }
        if (mc.getNetworkHandler() == null)
        {
            // TODO: port to 1.21.11 - mc.world.disconnect();
            this.toggle();

            // TODO: 1.21.11 - return;
        }
        disconnectReason = String.format(disconnectReason, args);
        mc.getNetworkHandler().getConnection().disconnect(Text.of(disconnectReason));
        this.toggle();
    }

    // TODO: 1.21.11 - private boolean checkEnemy(AbstractClientPlayerEntity player)
    {
        // TODO 1.21.11: return player.getDisplayName() != null && !FriendManager.INSTANCE.isFriend(player) && !player.equals(FakePlayer.INSTANCE.fakePlayer);
    }

    @Override
    public String getDescription()
    {
        return "AutoLog: Attempts disconnect when needed";
    }
}
