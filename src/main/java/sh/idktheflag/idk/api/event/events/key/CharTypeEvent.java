package sh.idktheflag.idk.api.event.events.key;

import lombok.Getter;
import sh.idktheflag.idk.api.event.Event;

@Getter
public class CharTypeEvent extends Event {
    private final char character;
    public CharTypeEvent(char character)
    {
        this.character = character;
    }
}
