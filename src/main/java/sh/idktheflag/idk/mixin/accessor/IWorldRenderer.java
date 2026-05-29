package sh.idktheflag.idk.mixin.accessor;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.BlockBreakingInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface IWorldRenderer {
    // TODO: 1.21.11 - frustum field removed from WorldRenderer

    @Accessor("blockBreakingInfos")
    Int2ObjectMap<BlockBreakingInfo> getBlockBreakingProgressions();

    @Accessor("bufferBuilders")
    BufferBuilderStorage hookGetBufferBuilders();

    @Accessor("entityRenderCommandQueue")
    net.minecraft.client.render.command.OrderedRenderCommandQueueImpl getEntityRenderCommandQueue();

    // TODO: 1.21.11 - noCullingBlockEntities removed from WorldRenderer
}
