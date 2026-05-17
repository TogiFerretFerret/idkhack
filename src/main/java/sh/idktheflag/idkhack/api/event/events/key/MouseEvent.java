package sh.idktheflag.idkhack.api.event.events.key;

import sh.idktheflag.idkhack.api.event.Event;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MouseEvent extends Event {
    int button;
    Type type;

    public enum Type {
        LIFT, CLICK;

        public static Type of(int id)
        {
            Preconditions.checkElementIndex(id, 2);
            return Type.values()[id];
        }
    }
}