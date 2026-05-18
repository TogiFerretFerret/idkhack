package sh.idktheflag.idk.api.event.events.network;

import sh.idktheflag.idk.api.event.Event;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

/**
 * @see sh.idktheflag.idk.mixin.MixinClientPlayNetworkHandler
 */
public class ServerEvent {

    public static class ServerLeft extends Event {

    }

    public static class ServerJoined extends Event {

    }

    public static class AttemptConnect extends Event {
        public ServerAddress address;
        public ServerInfo info;

        public AttemptConnect(ServerAddress address, ServerInfo info)
        {
            this.address = address;
            this.info = info;
        }
    }
}
