package sh.idktheflag.idk.impl.features.modules.render;


import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.render.world.RenderType;
import sh.idktheflag.idk.impl.features.hud.FeatureList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

import java.text.DecimalFormat;

public class BlockHighlight extends Module {
    public static BlockHighlight INSTANCE;

    Value<IdkColor> fill = new ValueBuilder<IdkColor>()
            .withDescriptor("Fill")
            .withValue(new IdkColor(255, 62, 62, 25))
            .register(this);
    Value<IdkColor> line = new ValueBuilder<IdkColor>()
            .withDescriptor("Line")
            .withValue(new IdkColor(255, 62, 62, 255))
            .register(this);
    Value<Boolean> entity = new ValueBuilder<Boolean>()
            .withDescriptor("Entity")
            .withValue(false)
            .register(this);
    Value<IdkColor> entityFill = new ValueBuilder<IdkColor>()
            .withDescriptor("Entity Fill")
            .withValue(new IdkColor(255, 62, 62, 25))
            .withParentEnabled(true)
            .withParent(entity)
            .register(this);
    Value<IdkColor> entityLine = new ValueBuilder<IdkColor>()
            .withDescriptor("Entity Line")
            .withValue(new IdkColor(255, 62, 62, 255))
            .withParent(entity)
            .withParentEnabled(true)

            .register(this);

    public BlockHighlight()
    {
        super("BlockHighlight", Category.Render);
        INSTANCE = this;
    }

    private double distance = 0;


    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event)
    {
        if (NullUtils.nullCheck()) return;

        Box render = null;
        final HitResult result = mc.crosshairTarget;
        if (result != null)
        {
            final Vec3d pos = mc.player.getEyePos();
            if (entity.getValue()
                    && result.getType() == HitResult.Type.ENTITY)
            {
                final Entity entity = ((EntityHitResult) result).getEntity();
                render = entity.getBoundingBox();
                distance = pos.distanceTo(new Vec3d(entity.getX(), entity.getY(), entity.getZ()));
            } else if (result.getType() == HitResult.Type.BLOCK)
            {
                BlockPos hpos = ((BlockHitResult) result).getBlockPos();
                BlockState state = mc.world.getBlockState(hpos);
                VoxelShape outlineShape = state.getOutlineShape(mc.world, hpos);
                if (outlineShape.isEmpty())
                {
                    return;
                }
                Box render1 = outlineShape.getBoundingBox();
                render = new Box(hpos.getX() + render1.minX, hpos.getY() + render1.minY,
                        hpos.getZ() + render1.minZ, hpos.getX() + render1.maxX,
                        hpos.getY() + render1.maxY, hpos.getZ() + render1.maxZ);
                distance = pos.distanceTo(hpos.toCenterPos());
            }
        }
        if (render != null)
        {
            if (result.getType() == HitResult.Type.ENTITY)
            {
                RenderUtil.renderBox(RenderType.FILL, render, entityFill.getValue().getColor(), entityFill.getValue().getColor());
                RenderUtil.renderBox(RenderType.LINES, render, entityLine.getValue().getColor(), entityLine.getValue().getColor());
            } else
            {
                RenderUtil.renderBox(RenderType.FILL, render, fill.getValue().getColor(), fill.getValue().getColor());
                RenderUtil.renderBox(RenderType.LINES, render, line.getValue().getColor(), line.getValue().getColor());
            }
        }
    }

    @Override
    public String getDescription()
    {
        return "BlockHighlight: highlights the block you are looking at";
    }


    @Override
    public String getHudInfo()
    {
        DecimalFormat decimal = new DecimalFormat("0.0");
        return decimal.format(distance);
    }

}