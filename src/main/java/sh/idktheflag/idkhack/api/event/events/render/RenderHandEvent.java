package sh.idktheflag.idkhack.api.event.events.render;


import lombok.Getter;
import sh.idktheflag.idkhack.api.event.Event;
import net.minecraft.client.util.math.MatrixStack;

@Getter
public class RenderHandEvent extends Event {
    private final MatrixStack matrices;
    private final float tickDelta;
    public RenderHandEvent(MatrixStack matrices, float tickDelta)
    {
        this.matrices = matrices;
        this.tickDelta = tickDelta;

    }

}