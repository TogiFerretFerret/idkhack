package sh.idktheflag.idkhack.mixin;

import sh.idktheflag.idkhack.api.utils.math.MathUtil;
import sh.idktheflag.idkhack.impl.features.commands.AutoRegearCommand;
import sh.idktheflag.idkhack.impl.features.modules.player.ChestStealer;
import sh.idktheflag.idkhack.impl.features.modules.player.Tweaks;
import sh.idktheflag.idkhack.impl.features.modules.render.Tooltips;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.component.Component;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T>
{
    @Shadow
    protected Slot focusedSlot;

    @Shadow
    protected int x;
    @Shadow
    protected int y;

    @Shadow
    public abstract T getScreenHandler();

    @Shadow
    public abstract void close();

    @Unique
    private static final ItemStack[] ITEMS = new ItemStack[27];

    public MixinHandledScreen(Text title)
    {
        super(title);
    }


    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info)
    {


        if (Tweaks.INSTANCE.regearButton.getValue())
            addDrawableChild(
                    new ButtonWidget.Builder(Text.literal("Save Regear"), button -> AutoRegearCommand.save("kit" + MathUtil.randomInt(0, 32767)))
                            .position(2, 2)
                            .size(70, 17)
                            .build()
            );

        if (Tweaks.INSTANCE.stealButton.getValue() && this.getScreenHandler() instanceof GenericContainerScreenHandler)
            addDrawableChild(
                    new ButtonWidget.Builder(Text.literal("Steal"), button -> ChestStealer.INSTANCE.toggle())
                            .position(x - 32, y)
                            .size(30, 17)
                            .build()
            );

    }


    @Inject(method = "drawMouseoverTooltip", at = @At(value = "HEAD"), cancellable = true)
    private void hookDrawMouseoverTooltip(DrawContext context, int x, int y, CallbackInfo ci)
    {
        if (Tooltips.INSTANCE == null || !Tooltips.INSTANCE.isEnabled() || !Tooltips.INSTANCE.shulkers.getValue()) return;

        if (focusedSlot == null)
        {
            return;
        }
        ItemStack stack = focusedSlot.getStack();
        if (stack.contains(DataComponentTypes.CONTAINER))
        {
            ContainerComponent containerComponent = stack.get(DataComponentTypes.CONTAINER);
            List<ItemStack> items = containerComponent.stream().toList();
            if (!items.isEmpty())
            {
                renderShulkerTooltip(context, items, x, y);
                ci.cancel();
            }
        }
    }

    @Unique
    private void renderShulkerTooltip(DrawContext context, List<ItemStack> items, int x, int y) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int rows = (int) Math.ceil(items.size() / 9.0);
        int cols = Math.min(items.size(), 9);
        
        int width = cols * 18 + 4;
        int height = rows * 18 + 4;
        
        int drawX = x + 12;
        int drawY = y - 12;
        
        context.fill(drawX, drawY, drawX + width, drawY + height, 0xFF000000);
        context.fill(drawX, drawY, drawX + width, drawY + 1, 0xFFFFFFFF);
        context.fill(drawX, drawY + height - 1, drawX + width, drawY + height, 0xFFFFFFFF);
        context.fill(drawX, drawY, drawX + 1, drawY + height, 0xFFFFFFFF);
        context.fill(drawX + width - 1, drawY, drawX + width, drawY + height, 0xFFFFFFFF);
        
        for (int i = 0; i < items.size(); i++) {
            int ix = drawX + 2 + (i % 9) * 18;
            int iy = drawY + 2 + (i / 9) * 18;
            context.drawItem(items.get(i), ix, iy);
            context.drawStackOverlay(mc.textRenderer, items.get(i), ix, iy);
        }
    }
}