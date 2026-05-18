package sh.idktheflag.idk.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import sh.idktheflag.idk.api.utils.ducks.IClientPlayNetworkHandler;
import sh.idktheflag.idk.mixin.accessor.IClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler implements IClientPlayNetworkHandler {

    @Shadow
    public abstract ClientConnection getConnection();

    @Override
    public void sendQuietPacket(final Packet<?> packet) {
        ((IClientConnection) getConnection()).sendQuietPacket(packet, null, true);
    }
}
