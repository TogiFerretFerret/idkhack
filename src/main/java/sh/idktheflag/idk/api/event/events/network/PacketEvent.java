package sh.idktheflag.idk.api.event.events.network;

import sh.idktheflag.idk.api.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;


@Getter
@Setter
public class PacketEvent extends Event
{
    private Packet<?> packet;

    public PacketEvent(Packet<?> packet)
    {
        this.packet = packet;
    }

    @Getter
    public static class Receive extends PacketEvent
    {
        BundleS2CPacket bundleS2CPacket;
        public Receive(Packet<?> packet)
        {
            super(packet);
            bundleS2CPacket = null;
        }

        public Receive(Packet<?> packet, BundleS2CPacket bundleS2CPacket)
        {
            super(packet);
            this.bundleS2CPacket = bundleS2CPacket;
        }

        public boolean isBundled()
        {
            return bundleS2CPacket != null;
        }
    }


    public static class ReceivePost extends PacketEvent
    {
        public ReceivePost(Packet<?> packet)
        {
            super(packet);
        }
    }


    public static class Send extends PacketEvent
    {
        public Send(Packet<?> packet)
        {
            super(packet);
        }
    }

    public static class SendPost extends PacketEvent
    {
        public SendPost(Packet<?> packet)
        {
            super(packet);
        }
    }
}
