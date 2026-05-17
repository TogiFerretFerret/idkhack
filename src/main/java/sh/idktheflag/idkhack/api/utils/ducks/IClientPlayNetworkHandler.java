package sh.idktheflag.idkhack.api.utils.ducks;

import net.minecraft.network.packet.Packet;

public interface IClientPlayNetworkHandler {
    void sendQuietPacket(final Packet<?> packet);

}