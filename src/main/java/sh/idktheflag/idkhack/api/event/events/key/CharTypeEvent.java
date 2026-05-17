package sh.idktheflag.idkhack.api.event.events.key;

import lombok.Getter;
import sh.idktheflag.idkhack.api.event.Event;

@Getter
public class CharTypeEvent extends Event {
    private final char character;
    public CharTypeEvent(char character)
    {
        this.character = character;
    }
}
