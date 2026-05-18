package sh.idktheflag.idkhack.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import sh.idktheflag.idkhack.api.utils.render.world.layer.idkhackLayers;
import net.minecraft.client.render.BufferBuilderStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BufferBuilderStorage.class})
public abstract class MixinBufferBuilderStorage {


    @Inject(method = "method_54639(Lit/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenHashMap;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;assignBufferBuilder(Lit/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenHashMap;Lnet/minecraft/client/render/RenderLayer;)V"))
    private void hookInit(Object2ObjectLinkedOpenHashMap map, CallbackInfo ci)
    {
        assignBufferBuilder(map, idkhackLayers.ENCHANT);
    }


    @Shadow
    private static void assignBufferBuilder(Object2ObjectLinkedOpenHashMap map, RenderLayer renderLayer) {}
}