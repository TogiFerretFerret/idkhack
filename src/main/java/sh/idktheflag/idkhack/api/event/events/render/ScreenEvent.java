package sh.idktheflag.idkhack.api.event.events.render;

import lombok.Setter;
import sh.idktheflag.idkhack.api.event.Event;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;

public class ScreenEvent {

    @Setter
    @Getter
    public static class SetScreen extends Event {

        Screen guiScreen;

        public SetScreen(Screen guiScreen)
        {
            this.guiScreen = guiScreen;
        }
    }


}
