package sh.idktheflag.idk.api.event.events.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sh.idktheflag.idk.api.event.Event;
@Getter
@AllArgsConstructor
public class ClientChatEvent extends Event {
    public String message;
}
