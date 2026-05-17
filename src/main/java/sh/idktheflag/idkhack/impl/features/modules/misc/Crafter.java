package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.StringUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;

import java.util.List;


public class Crafter extends Module
{

    public Value<String> craftingItem = new ValueBuilder<String>()
            .withDescriptor("Item")
            .withValue("stick")
            .register(this);

    public Crafter()
    {
        super("Crafter", Category.Misc);
    }


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent.Pre event)
    {
        if (NullUtils.nullCheck())
        {
            return;
        }


        Item targetItem = findItem(craftingItem.getValue());

        if (targetItem == null) return;

        List<RecipeResultCollection> recipeResultCollectionList = mc.player.getRecipeBook().getOrderedResults();
        for (RecipeResultCollection recipeResultCollection : recipeResultCollectionList)
        {
            // TODO: port to 1.21.11 - for (RecipeEntry<?> recipe : recipeResultCollection.getRecipes(true))
            {
                // TODO: port to 1.21.11 - final Item item = recipe.value().getResult(mc.world.getRegistryManager()).getItem();

                if (false) // TODO 1.21.11: if (item.equals(targetItem))
                {
                    // TODO: port to 1.21.11 - mc.interactionManager.clickRecipe(mc.player.currentScreenHandler.syncId, recipe, true);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 0, 1, SlotActionType.QUICK_MOVE, mc.player);
                }
            }
        }
    }

    private Item findItem(String name)
    {
        return StringUtils.parseId(Registries.ITEM, name);
    }

    @Override
    public String getDescription()
    {
        return "Crafter: crafts sticks";
    }


}
