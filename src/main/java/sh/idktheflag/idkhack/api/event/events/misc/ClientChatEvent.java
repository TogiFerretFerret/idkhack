package sh.idktheflag.idkhack.api.event.events.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sh.idktheflag.idkhack.api.event.Event;
@Getter
@AllArgsConstructor
public class ClientChatEvent extends Event {
    public String message;
}
