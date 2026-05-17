package sh.idktheflag.idkhack.api.event.events.move;

import lombok.Getter;
import sh.idktheflag.idkhack.api.event.Event;
import net.minecraft.entity.Entity;

public class PushEvent {
    @Getter
    public static class Entities extends Event {
        private final Entity pushed, pusher;

        public Entities(Entity pushed, Entity pusher)
        {
            this.pushed = pushed;
            this.pusher = pusher;
        }

    }


    public static class Blocks extends Event {
        public Blocks()
        {
        }

    }
}