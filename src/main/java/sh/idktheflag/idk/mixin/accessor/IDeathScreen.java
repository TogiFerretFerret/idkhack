package sh.idktheflag.idk.mixin.accessor;

import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DeathScreen.class)
public interface IDeathScreen {
    @Accessor("message")
    Text getDeathMessage();

    @Accessor("isHardcore")
    boolean isScreenHardcore();
}
