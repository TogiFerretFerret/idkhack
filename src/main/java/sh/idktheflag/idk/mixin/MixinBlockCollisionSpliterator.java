package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.api.event.events.world.CollisionBoxEvent;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.CollisionView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockCollisionSpliterator.class)
public class MixinBlockCollisionSpliterator implements IMinecraft {

    @Redirect(method = "computeNext",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/ShapeContext;getCollisionShape(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/CollisionView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;"))
    private VoxelShape hookGetCollisionShape(ShapeContext shapeContext, BlockState blockState, CollisionView world, BlockPos blockPos)
    {
        VoxelShape voxelShape = shapeContext.getCollisionShape(blockState, world, blockPos);
        if (world != mc.world)
        {
            return voxelShape;
        }
        CollisionBoxEvent blockCollisionEvent = new CollisionBoxEvent(voxelShape, blockPos, blockState);
        blockCollisionEvent.post();
        if (blockCollisionEvent.isCancelled())
        {
            return blockCollisionEvent.getVoxelShape();
        }
        return voxelShape;
    }
}
