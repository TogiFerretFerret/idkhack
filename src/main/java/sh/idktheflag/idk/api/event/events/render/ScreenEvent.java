package sh.idktheflag.idk.api.event.events.render;

import lombok.Setter;
import sh.idktheflag.idk.api.event.Event;
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
