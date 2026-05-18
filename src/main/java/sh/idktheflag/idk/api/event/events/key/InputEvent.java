package sh.idktheflag.idk.api.event.events.key;

import sh.idktheflag.idk.api.event.Event;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import net.minecraft.client.input.Input;
import net.minecraft.network.packet.Packet;

public class InputEvent extends Event {

    public Input input;

    public InputEvent(Input input)
    {
        this.input = input;
    }

}