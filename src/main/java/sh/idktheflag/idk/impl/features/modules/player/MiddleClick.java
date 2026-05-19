package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idk.api.gui.font.Fonts;
import sh.idktheflag.idk.api.utils.players.InventoryUtils;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.utils.render.ScaledResolution;
import sh.idktheflag.idk.impl.features.modules.client.HudColors;
import sh.idktheflag.idk.impl.features.modules.combat.FastProjectile;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.FriendManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.chat.ChatMessage;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.features.modules.movement.Flight;
import sh.idktheflag.idk.impl.features.modules.movement.LongJump;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.awt.*;

public class MiddleClick extends Module
{
    public static MiddleClick INSTANCE;
    Value<Boolean> friend = new ValueBuilder<Boolean>()
            .withDescriptor("Friend")
            .withValue(true)
            .register(this);
    Value<Boolean> pearl = new ValueBuilder<Boolean>()
            .withDescriptor("Pearl")
            .withValue(true)
            .register(this);
    Value<Boolean> inventory = new ValueBuilder<Boolean>()
            .withDescriptor("Inventory")
            .withValue(false)
            .withParentEnabled(true)
            .withParent(pearl)
            .register(this);
    Value<Boolean> fastProjectile = new ValueBuilder<Boolean>()
            .withDescriptor("Charge")
            .withValue(true)
            .withParent(pearl)
            .withParentEnabled(true)
            .register(this);
    Value<Boolean> fireworks = new ValueBuilder<Boolean>()
            .withDescriptor("Fireworks")
            .withValue(true)
            .register(this);

    public MiddleClick()
    {
        super("MiddleClick", Category.Player);
        INSTANCE = this;
    }

    boolean hasPressed = false;
    boolean startedTimer = false;
    long startTime = 0;
    public boolean fireworkSchedule = false;

    @SubscribeEvent
    public void onTickPre(TickEvent.PlayerTickEvent.Pre event)
    {
        if (NullUtils.nullCheck()) return;

        if (mc.mouse.wasMiddleButtonClicked())
        {
            if (!false && (!fireworks.getValue() || !mc.player.isGliding()))
            {
                if (pearl.getValue() && fastProjectile.getValue())
                {
                    if (!startedTimer)
                    {
                        startTime = System.currentTimeMillis();
                        startedTimer = true;
                    }
                }
            }
            final Entity pointed = mc.targetedEntity;
            if (!hasPressed)
            {
                if (startedTimer) return;


                if (friend.getValue())
                {
                    if (pointed != null && pointed instanceof PlayerEntity)
                    {
                        if (FriendManager.INSTANCE.isFriend(pointed))
                        {
                            FriendManager.INSTANCE.removeFriend(pointed);
                            ChatUtils.sendMessage(new ChatMessage(
                                    "Removed " + pointed.getName().getString() + " from friends",
                                    false,
                                    0
                            ));
                            hasPressed = true;
                        } else
                        {
                            FriendManager.INSTANCE.addFriend(pointed);
                            ChatUtils.sendMessage(new ChatMessage(
                                    "Added " + pointed.getName().getString() + " from friends",
                                    false,
                                    0
                            ));
                            hasPressed = true;
                        }
                    }
                }

                if (pointed == null)
                {

                    if (fireworks.getValue())
                    {


                        if (Flight.isGrimFlying() || LongJump.isGrimJumping())
                        {
                            hasPressed = true;
                            fireworkSchedule = true;
                            return;

                        } else if (mc.player.isGliding())
                        {
                            doFirework();
                            hasPressed = true;
                            return;
                        }
                    }
                    if (pearl.getValue() && !fastProjectile.getValue())
                    {
                        doPearl();
                    }
                }
            }

        } else
        {
            if (pearl.getValue() && fastProjectile.getValue())
            {
                if (startedTimer)
                    doPearl();
            }
            startedTimer = false;
            hasPressed = false;
        }

    }

    public void doPearl()
    {
        int pearlSlot = InventoryUtils.getHotbarItemSlot(Items.ENDER_PEARL);
        hasPressed = true;

        if (pearlSlot == -1)
        {

            if(!inventory.getValue())
            {
                ChatUtils.sendMessage(new ChatMessage(
                        "No pearls in hotbar",
                        false,
                        0
                ));
            }else
            {
                int pearlInv = InventoryUtils.getInventoryItemSlot(Items.ENDER_PEARL);
                if (pearlInv == -1)
                {
                    ChatUtils.sendMessage(new ChatMessage(
                            "No fireworks in inv",
                            false,
                            0
                    ));
                    return;
                }
                InventoryUtils.swap(pearlInv, mc.player.getInventory().getSelectedSlot());
                PlayerUtils.use();
                InventoryUtils.swap(pearlInv, mc.player.getInventory().getSelectedSlot());
            }
            return;
        }

        switchAndUse(pearlSlot, true);
    }

    public void doFirework()
    {

        int fireworkHotbar = InventoryUtils.getHotbarItemSlot(Items.FIREWORK_ROCKET);
        if (fireworkHotbar != -1)
        {
            switchAndUse(fireworkHotbar, false);
        } else
        {
            int fireworkInv = InventoryUtils.getInventoryItemSlot(Items.FIREWORK_ROCKET);
            if (fireworkInv == -1)
            {
                ChatUtils.sendMessage(new ChatMessage(
                        "No fireworks in inv",
                        false,
                        0
                ));
                return;
            }
            InventoryUtils.swap(fireworkInv, mc.player.getInventory().getSelectedSlot());
            PlayerUtils.use();
            InventoryUtils.swap(fireworkInv, mc.player.getInventory().getSelectedSlot());
        }
    }


    public void switchAndUse(int slot, boolean pearl)
    {
        int oldSlot = mc.player.getInventory().getSelectedSlot();
        InventoryUtils.switchToSlotGhost(slot);
        if (pearl && fastProjectile.getValue() && System.currentTimeMillis() - startTime > 500)
        {
            FastProjectile.INSTANCE.sendPackets();
        }
        PlayerUtils.use();
        InventoryUtils.switchToSlotGhost(oldSlot);
    }

    @SubscribeEvent
    public void draw(RenderGameOverlayEvent.Text event)
    {
        if (NullUtils.nullCheck()) return;


        if (startedTimer)
        {
            final ScaledResolution resolution = new ScaledResolution(mc);

            final float x = resolution.getScaledWidth() / 2.0F;
            final float y = resolution.getScaledHeight() / 2.0F + 13;
            final float thickness = 1f;
            float percentage = Math.min(1f, (System.currentTimeMillis() - startTime) / (500f));

            final float width = 80.0F;
            final float half = width / 2;
            Color left = HudColors.getTextColor(0);
            Color right = HudColors.getTextColor((int) Fonts.getTextHeight("AA"));

            event.getContext().fill((int) (x - half - 0.5), (int) (y - 0.5), (int) ((x - half - 0.5) + (half * 2) + 1f), (int) ((y - 0.5) + thickness + 1), 0x78000000);
            // In 1.21.11, gradient filling can be done via context.fillGradient, but it takes coordinates instead of width/height
            // Not doing complex gradients for now to keep it simple, just using left color
            event.getContext().fill((int) (x - half), (int) y, (int) ((x - half) + width * percentage), (int) (y + thickness), left.getRGB());
        }
    }


    boolean isAutoXP()
    {
        return false; // AutoXP module removed
    }

    @Override
    public String getHudInfo()
    {
        if (mc.player != null && mc.player.isGliding() && fireworks.getValue())
            return "Firework";

        return "Pearl";
    }


    @Override
    public String getDescription()
    {
        return "MiddleClick: does various actions on middle click";
    }

}
