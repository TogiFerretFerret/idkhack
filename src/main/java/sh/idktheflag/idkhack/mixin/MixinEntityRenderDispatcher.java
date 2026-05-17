package sh.idktheflag.idkhack.mixin;

import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// EntityRenderDispatcher was renamed to EntityRenderManager
// render method signature changed completely (uses render states now)
@Mixin(EntityRenderManager.class)
public class MixinEntityRenderDispatcher<T extends Entity>
{

}
