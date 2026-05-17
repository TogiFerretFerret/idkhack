package sh.idktheflag.idkhack.mixin;

import sh.idktheflag.idkhack.api.event.events.world.CollisionBoxEvent;
import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import sh.idktheflag.idkhack.impl.IdkHackMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockCollisionSpliterator.class)
public class MixinBlockCollisionSpliterator implements IMinecraft {
    @Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/" + "block/BlockState;getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/" + "util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"))
    private VoxelShape hookGetCollisionShape(BlockState instance, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext)
    {
        VoxelShape voxelShape = instance.getCollisionShape(blockView, blockPos, shapeContext);
        if (blockView != mc.world)
        {
            return voxelShape;
        }
        CollisionBoxEvent blockCollisionEvent = new CollisionBoxEvent(voxelShape, blockPos, instance);
        blockCollisionEvent.post();
        if (blockCollisionEvent.isCancelled())
        {
            return blockCollisionEvent.getVoxelShape();
        }
        return voxelShape;
    }
}