package sh.idktheflag.idkhack.api.event.events.move;

import sh.idktheflag.idkhack.api.event.Event;
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