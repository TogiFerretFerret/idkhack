package sh.idktheflag.idkhack.impl.features.modules.player;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.Timer;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import sh.idktheflag.idkhack.api.utils.players.InventoryUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {


    public static AutoArmor INSTANCE;

    public AutoArmor()
    {
        super("AutoArmor", Category.Player);
        INSTANCE = this;
    }


    Value<Number> delay = new ValueBuilder<Number>()
            .withDescriptor("Delay", "delay")
            .withValue(100)
            .withRange(1, 1000)
            .register(this);
    Value<Boolean> pantsBlastPrio = new ValueBuilder<Boolean>()
            .withDescriptor("Blast Pants")
            .withValue(false)
            .register(this);
    Timer delayTimer = new Timer();



    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;


        for (int i = 3; i >= 0; i--)
        {
            // TODO: port to 1.21.11 - if (mc.player.getInventory().armor.get(i).isEmpty())
            {
                if (equipArmor(i))
                    break;
            }
        }
    }


    private boolean equipArmor(int slot)
    {
        ArmorType armorType = getArmorTypeFromSlot(slot);
        int bestSlot = -1;
        int bestRating = -1;

        for (int i = 0; i < 35; i++)
        {
            ItemStack stack = mc.player.getInventory().getStack(i);
            EquippableComponent equippable = stack.get(DataComponentTypes.EQUIPPABLE);

            if (equippable != null && getTypeFromSlot(equippable.slot()) == armorType)
            {
                int damageReduction = stack.getItem().getDefaultStack().getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, null) != null ? getArmorValue(stack) : 0;
                if (getTypeFromSlot(equippable.slot()) == ArmorType.PANTS && pantsBlastPrio.getValue())
                {
                    if (InventoryUtils.getEnchantmentLevel(stack, Enchantments.BLAST_PROTECTION) > 0)
                    {
                        bestSlot = i;
                        bestRating = damageReduction;
                    }
                } else
                {
                    if (damageReduction >= bestRating)
                    {
                        bestSlot = i;
                        bestRating = damageReduction;
                    }
                }
            }
        }

        if (bestSlot != -1 && bestRating != -1)
        {
            if (delayTimer.isPassed(delay.getValue().longValue()))
            {
                if (bestSlot < 9) bestSlot = InventoryUtils.hotbarToInventory(bestSlot);

                mc.interactionManager.clickSlot(0, bestSlot, 0, SlotActionType.QUICK_MOVE, mc.player);
                delayTimer.resetDelay();
                return true;
            }
        }
        return false;
    }

    private int getArmorValue(ItemStack stack)
    {
        // Approximate armor value based on item
        Item item = stack.getItem();
        if (item == Items.NETHERITE_HELMET || item == Items.NETHERITE_CHESTPLATE || item == Items.NETHERITE_LEGGINGS || item == Items.NETHERITE_BOOTS) return 3;
        if (item == Items.DIAMOND_HELMET || item == Items.DIAMOND_CHESTPLATE || item == Items.DIAMOND_LEGGINGS || item == Items.DIAMOND_BOOTS) return 3;
        if (item == Items.IRON_HELMET || item == Items.IRON_CHESTPLATE || item == Items.IRON_LEGGINGS || item == Items.IRON_BOOTS) return 2;
        return 1;
    }

    private ArmorType getTypeFromSlot(EquipmentSlot slot)
    {
        return switch (slot)
        {
            case HEAD -> ArmorType.HELMET;
            case CHEST -> ArmorType.CHESTPLATE;
            case LEGS -> ArmorType.PANTS;
            case FEET -> ArmorType.BOOTS;
            default -> null;
        };
    }


    private ArmorType getTypeFromItem(Item item)
    {
        if (Items.NETHERITE_HELMET.equals(item) || Items.DIAMOND_HELMET.equals(item) || Items.GOLDEN_HELMET.equals(item) || Items.IRON_HELMET.equals(item) || Items.CHAINMAIL_HELMET.equals(item) || Items.LEATHER_HELMET.equals(item))
        {
            return ArmorType.HELMET;
        } else if (Items.NETHERITE_CHESTPLATE.equals(item)  || Items.DIAMOND_CHESTPLATE.equals(item) || Items.GOLDEN_CHESTPLATE.equals(item) || Items.IRON_CHESTPLATE.equals(item) || Items.CHAINMAIL_CHESTPLATE.equals(item) || Items.LEATHER_CHESTPLATE.equals(item))
        {
            return ArmorType.CHESTPLATE;
        } else if (Items.NETHERITE_LEGGINGS.equals(item) || Items.DIAMOND_LEGGINGS.equals(item) || Items.GOLDEN_LEGGINGS.equals(item) || Items.IRON_LEGGINGS.equals(item) || Items.CHAINMAIL_LEGGINGS.equals(item) || Items.LEATHER_LEGGINGS.equals(item))
        {
            return ArmorType.PANTS;
        } else if (Items.NETHERITE_BOOTS.equals(item) || Items.DIAMOND_BOOTS.equals(item) || Items.GOLDEN_BOOTS.equals(item) || Items.IRON_BOOTS.equals(item) || Items.CHAINMAIL_BOOTS.equals(item) || Items.LEATHER_BOOTS.equals(item))
        {
            return ArmorType.BOOTS;
        }
        return null;
    }

    private ArmorType getArmorTypeFromSlot(int slot)
    {
        switch (slot)
        {
            case 3:
                return ArmorType.HELMET;
            case 2:
                return ArmorType.CHESTPLATE;
            case 1:
                return ArmorType.PANTS;
            case 0:
                return ArmorType.BOOTS;
            default:
                return null;
        }
    }

    private int getSlotByType(ArmorType type)
    {
        switch (type)
        {
            case HELMET:
                return 5;
            case CHESTPLATE:
                return 6;
            case PANTS:
                return 7;
            case BOOTS:
                return 8;
            default:
                return -1;
        }
    }

    private enum ArmorType {
        HELMET,
        CHESTPLATE,
        PANTS,
        BOOTS;
    }

    @Override
    public String getDescription()
    {
        return "AutoArmor: Equips Armor automatically";
    }
}
