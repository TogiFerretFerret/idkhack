package sh.idktheflag.idk.api.event.events.key;

import sh.idktheflag.idk.api.event.Event;
import lombok.Getter;

@Getter
public class KeyboardEvent extends Event {
    private final boolean state;
    private final int key;
    private final int action;

    public KeyboardEvent(boolean state, int key, int action)
    {
        this.state = state;
        this.key = key;
        this.action = action;
    }
}
