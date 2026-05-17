package sh.idktheflag.idkhack.api.event.events.player;

import lombok.Getter;
import lombok.Setter;
import sh.idktheflag.idkhack.api.event.Event;

/**
 * @see sh.idktheflag.idkhack.mixin.MixinClientPlayerInteractionManager
 */
@Getter
@Setter
public class ReachEvent extends Event {
    public float reach;
    
    public ReachEvent(float reach)
    {
        this.reach = reach;
    }

}
