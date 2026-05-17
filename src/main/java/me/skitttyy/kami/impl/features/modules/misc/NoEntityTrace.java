package me.skitttyy.kami.impl.features.modules.misc;

import me.skitttyy.kami.api.feature.module.Module;
import me.skitttyy.kami.api.value.Value;
import me.skitttyy.kami.api.value.builder.ValueBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class NoEntityTrace extends Module {
    private static NoEntityTrace INSTANCE;


    public NoEntityTrace()
    {
        super("NoEntityTrace", Category.Misc);
        INSTANCE = this;
    }

    Value<Boolean> Pickaxe = new ValueBuilder<Boolean>()
            .withDescriptor("Pickaxe")
            .withValue(false)
            .withAction(s ->
            {
            })
            .register(this);

    public static boolean spoofTrace()
    {
        if (!INSTANCE.isEnabled())
        {
            return false;
        }
        if (INSTANCE.Pickaxe.getValue() && !isPickaxe(mc.player.getMainHandStack().getItem()))
        {
            return false;
        }
        return true;
    }


    private static boolean isPickaxe(Item item)
    {
        return item == Items.DIAMOND_PICKAXE
                || item == Items.NETHERITE_PICKAXE
                || item == Items.IRON_PICKAXE
                || item == Items.GOLDEN_PICKAXE
                || item == Items.STONE_PICKAXE
                || item == Items.WOODEN_PICKAXE;
    }

    @Override
    public String getDescription()
    {
        return "NoEntityTrace: Makes your crosshair trace past entity's";
    }
}
