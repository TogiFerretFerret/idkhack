package sh.idktheflag.idk.api.event.events.player;

import lombok.Getter;
import lombok.Setter;
import sh.idktheflag.idk.api.event.Event;

/**
 * @see sh.idktheflag.idk.mixin.MixinClientPlayerInteractionManager
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
