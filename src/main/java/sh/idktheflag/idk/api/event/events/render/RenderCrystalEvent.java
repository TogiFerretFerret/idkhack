package sh.idktheflag.idk.api.event.events.render;

import lombok.Getter;
import lombok.Setter;
import sh.idktheflag.idk.api.event.Event;
import net.minecraft.client.render.entity.model.EndCrystalEntityModel;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;


@Getter
@Setter
public class RenderCrystalEvent extends Event
{
    public final EndCrystalEntityRenderState renderState;
    public final MatrixStack matrixStack;
    public final EndCrystalEntityModel model;

    public RenderCrystalEvent(EndCrystalEntityRenderState renderState, MatrixStack matrixStack, EndCrystalEntityModel model) {
        this.renderState = renderState;
        this.matrixStack = matrixStack;
        this.model = model;
    }
}
