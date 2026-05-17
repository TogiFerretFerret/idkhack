package sh.idktheflag.idkhack.mixin.accessor;

import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// ItemRenderer was completely rewritten:
// - builtinModelItemRenderer field is removed
// - renderBakedItemModel method is removed
// - ItemRenderer is now a simpler class with static methods (renderItem, getItemGlintConsumer, etc.)
@Mixin(ItemRenderer.class)
public interface IItemRenderer
{

}
