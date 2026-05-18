package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.api.gui.shulker.container.ContainerManager;
import sh.idktheflag.idk.api.gui.shulker.container.ContainerType;
import sh.idktheflag.idk.api.gui.shulker.positioners.CapacityBarRenderer;
import sh.idktheflag.idk.api.gui.shulker.positioners.IconRenderer;
import sh.idktheflag.idk.api.utils.ducks.IDrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DrawContext.class)
public abstract class MixinDrawContext implements IDrawContext {
    @Unique
    IconRenderer iconRenderer;
    @Unique
    boolean adjustSize = false;

    @Override
    public void adjustSize(boolean newValue)
    {
        adjustSize = newValue;
    }

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/item/ItemStack.isItemBarVisible()Z"),
            method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    private void renderShulkerItemOverlay(TextRenderer renderer, ItemStack stack, int x, int y, @Nullable String countLabel, CallbackInfo info)
    {


        if (!false) return;

        ContainerManager containerParser = new ContainerManager(stack);

        ItemStack displayStack = containerParser.getDisplayStack();

        if (displayStack == null) return;

// TODO: removed Tooltips - &
        {
            iconRenderer = new IconRenderer(containerParser, displayStack, x, y);
            iconRenderer.renderOptional((DrawContext) (Object) this);
        }
        // Display itemBar for containers. Ignore bundles - they already have this feature
        boolean isBundle = containerParser.getContainerType().equals(ContainerType.BUNDLE);
// TODO: removed Tooltips - &
        {
            CapacityBarRenderer capacityBarRenderer = new CapacityBarRenderer(containerParser, stack, x, y);
            capacityBarRenderer.renderOptional((DrawContext) (Object) this);
        }
    }

    @ModifyArgs(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/util/math/MatrixStack.translate(FFF)V"))
    private void injectedTranslateXYZ(Args args)
    {
		if (!false) return;

        if (adjustSize)
        {
            args.set(0, (float) args.get(0) + iconRenderer.xOffset);
            args.set(1, (float) args.get(1) + iconRenderer.yOffset);
            args.set(2, iconRenderer.zOffset);
        }
    }

    @ModifyArgs(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/util/math/MatrixStack.scale(FFF)V"))
    private void injectedScale(Args args)
    {
		if (!false) return;


        if (adjustSize)
        {
            args.set(0, iconRenderer.scale);
            args.set(1, -iconRenderer.scale);
            args.set(2, iconRenderer.scale);
        }
    }
}