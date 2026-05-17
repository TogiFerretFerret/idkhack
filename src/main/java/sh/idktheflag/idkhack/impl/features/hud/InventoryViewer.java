package sh.idktheflag.idkhack.impl.features.hud;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderGameOverlayEvent;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.utils.players.InventoryUtils;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sh.idktheflag.idkhack.api.feature.hud.HudComponent;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.item.Items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryViewer extends HudComponent
{
    public InventoryViewer()
    {
        super("InventoryViewer");
    }

    Value<Boolean> aboveItems = new ValueBuilder<Boolean>()
            .withDescriptor("Above Items")
            .withValue(true)
            .withAction(s -> handleVisibility(s.getValue()))
            .register(this);

    public Value<Boolean> Totems = new ValueBuilder<Boolean>()
            .withDescriptor("Totems")
            .withValue(false)
            .withParent(aboveItems)
            .withParentEnabled(true)
            .register(this);
    public Value<Boolean> Crystals = new ValueBuilder<Boolean>()
            .withDescriptor("Crystals")
            .withValue(false)
            .withParent(aboveItems)
            .withParentEnabled(true)
            .register(this);
    public Value<Boolean> Obsidian = new ValueBuilder<Boolean>()
            .withDescriptor("Obsidian")
            .withValue(false)
            .withParent(aboveItems)
            .withParentEnabled(true)
            .register(this);
    public Value<Boolean> XP = new ValueBuilder<Boolean>()
            .withDescriptor("XP")
            .withValue(false)
            .withParent(aboveItems)
            .withParentEnabled(true)
            .register(this);
    public Value<Sn0wColor> borderColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Border Color")
            .withValue(new Sn0wColor(255, 0, 0))
            .register(this);
    public Value<Sn0wColor> insideColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Inside Color")
            .withValue(new Sn0wColor(20, 20, 20))
            .register(this);
    public Value<Sn0wColor> textColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Text Color")
            .withValue(new Sn0wColor(255, 255, 255))
            .register(this);
    public Value<Boolean> snap = new ValueBuilder<Boolean>()
            .withDescriptor("Snap")
            .withValue(true)
            .register(this);

    @SubscribeEvent
    @Override
    public void draw(RenderGameOverlayEvent.Text event)
    {
        super.draw(event);

        if (NullUtils.nullCheck() || renderCheck(event)) return;


        this.width = ((17 * 9) + 10);
        this.height = (((17 * 3) + 3));
        if (snap.getValue())
        {
            this.yPos.setValue(event.getContext().getScaledWindowHeight() - 1 - height);
        }
        DrawContext context = event.getContext();
        context.getMatrices().pushMatrix();
        // TODO: port to 1.21.11 - Matrix3x2fStack has no z translate
        try
        {
            final List<ItemStack> items = mc.player.getInventory().getMainStacks();

            // TODO: port to 1.21.11 - DrawContext.getMatrices() now returns Matrix3x2fStack, use new MatrixStack for renderBox
            renderBox(new net.minecraft.client.util.math.MatrixStack(), xPos.getValue().intValue(), yPos.getValue().intValue());
            renderItems(context, items, xPos.getValue().intValue(), yPos.getValue().intValue() + 18);
            List<Item> ItemsList = new ArrayList<>();
            if (aboveItems.getValue())
            {
                if (Crystals.getValue())
                    ItemsList.add(Items.END_CRYSTAL);

                if (Totems.getValue())
                    ItemsList.add(Items.TOTEM_OF_UNDYING);

                if (Obsidian.getValue())
                    ItemsList.add(Items.OBSIDIAN);

                if (XP.getValue())
                    ItemsList.add(Items.EXPERIENCE_BOTTLE);


                int offset = 0;

                for (Item item : ItemsList)
                {
                    int count = InventoryUtils.getItemCount(item);

                    RenderUtil.renderItemWithCount(context, item.getDefaultStack(), new Point(xPos.getValue().intValue() + 1 + (20 * offset), yPos.getValue().intValue() - 18), count, textColor.getValue().getColor(), true);
                    offset++;

                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        context.getMatrices().popMatrix();
    }

    private void renderBox(MatrixStack matrices, final int x, final int y)
    {
        RenderUtil.renderRect(matrices, x, y, (17 * 9) + 10, (17 * 3) + 3, 1, insideColor.getValue().getColor());
        RenderUtil.renderOutlineRect(matrices, x, y, (17 * 9) + 10, (17 * 3) + 3, 1, borderColor.getValue().getColor(), true);
    }

    private void renderItems(DrawContext context, List<ItemStack> items, int x, int y)
    {
        for (int size = items.size(), item = 9; item < size; ++item)
        {
            final int slotx = x + 1 + (item - 9) % 9 * 18;
            final int sloty = y + 1 + ((item - 9) / 9 - 1) * 18;
            context.drawItem(items.get(item), slotx, sloty);
            context.drawStackOverlay(mc.textRenderer, items.get(item), slotx, sloty);
        }
    }


    static class ItemComponent
    {
        ItemStack theItemStack;

        public ItemComponent(ItemStack itemStack)
        {
            theItemStack = itemStack;
        }
    }


    public void handleVisibility(Boolean visible)
    {
        Crystals.setActive(visible);
        Totems.setActive(visible);
        XP.setActive(visible);
    }

    @Override
    public String getDescription()
    {
        return "InventoryViewer: Renders your inventory on screen";
    }
}