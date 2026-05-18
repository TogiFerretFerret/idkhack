package sh.idktheflag.idk.api.event.events.move;

import sh.idktheflag.idk.api.event.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalkingForwardEvent extends Event {

    boolean walksForward;

    public WalkingForwardEvent(boolean walksForward)
    {
        this.walksForward = walksForward;
    }
}