package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import sh.idktheflag.idkhack.api.utils.color.IdkColor;
import sh.idktheflag.idkhack.api.utils.math.MathUtil;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import sh.idktheflag.idkhack.api.utils.players.rotation.RotationUtils;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.world.RenderType;
import sh.idktheflag.idkhack.api.utils.render.world.buffers.RenderBuffers;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;


import java.awt.*;
import java.util.List;

public class Trajectories extends Module
{

    Value<Number> lineWidth = new ValueBuilder<Number>()
            .withDescriptor("Line Width")
            .withValue(2)
            .withRange(0.1, 5)
            .register(this);

    Value<IdkColor> startColor = new ValueBuilder<IdkColor>()
            .withDescriptor("Start Color")
            .withValue(new IdkColor(255, 255, 255))
            .register(this);
    Value<IdkColor> endColor = new ValueBuilder<IdkColor>()
            .withDescriptor("End Color")
            .withValue(new IdkColor(0, 255, 72))
            .register(this);
    Value<Boolean> pearls = new ValueBuilder<Boolean>()
            .withDescriptor("Pearls")
            .withValue(true)
            .register(this);

    public Trajectories()
    {
        super("Trajectories", Category.Render);
    }

    private MathUtil.Result result = null;


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;

    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event)
    {
        if (NullUtils.nullCheck()) return;


        if (pearls.getValue())
        {
            for (Entity entity : mc.world.getEntities())
            {
                if (entity instanceof EnderPearlEntity pearl)
                {
                    if(!mc.world.getWorldBorder().contains(new Vec3d(pearl.getX(), pearl.getY(), pearl.getZ()))) return;

                    MathUtil.Result result = MathUtil.calcTrajectory(pearl);

                    if (result != null)
                    {

                        drawTrail(result);
                        if (result.getHitResult() != null)
                        {
                            HitResult hitResult = result.getHitResult();
                            if (!hitResult.getType().equals(HitResult.Type.MISS))
                            {
                                if (hitResult instanceof BlockHitResult blockHitResult)
                                {
                                    RenderUtil.drawCircle(RenderBuffers.TRIANGLES, 0.3f, 130, blockHitResult.getPos(), blockHitResult.getSide(), ColorUtil.newAlpha(endColor.getValue().getColor(), 25));
                                    RenderUtil.drawCircle(RenderBuffers.LINES, 0.3f, 130, blockHitResult.getPos(), blockHitResult.getSide(), endColor.getValue().getColor());

                                } else if (hitResult instanceof EntityHitResult entityHitResult)
                                {
                                    RenderUtil.renderBox(RenderType.FILL, entityHitResult.getEntity().getBoundingBox(), ColorUtil.newAlpha(endColor.getValue().getColor().brighter(), 100), ColorUtil.newAlpha(endColor.getValue().getColor().brighter(), 100));
                                }
                            }
                        }
                    }
                }


            }
        }
        ItemStack mainHand = mc.player.getMainHandStack();
        ItemStack offHand = mc.player.getOffHandStack();
        Hand hand;

        if (mainHand.getItem() instanceof BowItem || mainHand.getItem() instanceof CrossbowItem || MathUtil.isThrowable(mainHand.getItem()))
        {
            hand = Hand.MAIN_HAND;
        } else if (offHand.getItem() instanceof BowItem || offHand.getItem() instanceof CrossbowItem || MathUtil.isThrowable(offHand.getItem()))
        {
            hand = Hand.OFF_HAND;
        } else return;

        MathUtil.Result result = MathUtil.calcTrajectory(hand == Hand.OFF_HAND ? offHand.getItem() : mainHand.getItem(), RotationUtils.getActualYaw());

        if (result != null)
        {

            drawTrail(result);
            if (result.getHitResult() != null)
            {
                HitResult hitResult = result.getHitResult();
                if (!hitResult.getType().equals(HitResult.Type.MISS))
                {
                    if (hitResult instanceof BlockHitResult blockHitResult)
                    {
                        RenderUtil.drawCircle(RenderBuffers.TRIANGLES, 0.3f, 130, blockHitResult.getPos(), blockHitResult.getSide(), ColorUtil.newAlpha(endColor.getValue().getColor(), 25));
                        RenderUtil.drawCircle(RenderBuffers.LINES, 0.3f, 130, blockHitResult.getPos(), blockHitResult.getSide(), endColor.getValue().getColor());

                    } else if (hitResult instanceof EntityHitResult entityHitResult)
                    {
                        RenderUtil.renderBox(RenderType.FILL, entityHitResult.getEntity().getBoundingBox(), ColorUtil.newAlpha(endColor.getValue().getColor().brighter(), 100), ColorUtil.newAlpha(endColor.getValue().getColor().brighter(), 100));
                    }
                }
            }
        }
    }

    public void drawTrail(MathUtil.Result result)
    {
        if (result.getPoints().isEmpty()) return;

        renderTrail(result, endColor.getValue().getColor(), startColor.getValue().getColor(), result.getPoints().get(0));
    }

    public void renderTrail(MathUtil.Result result, Color start, Color end, Vec3d first)
    {
        List<Vec3d> points = result.getPoints();
        Vec3d lastPos = first;
        for (int i = 0; i < points.size(); i++)
        {
            Vec3d p = points.get(i);
            if (p.equals(lastPos)) { lastPos = p; continue; }
            double value = normalize(i, 0, points.size());
            Color c = ColorUtil.interpolate((float) value, start, end);
            RenderUtil.drawWorldLine(lastPos, p, c, c);
            lastPos = p;
        }
    }

    double normalize(double value, double min, double max)
    {
        return ((value - min) / (max - min));
    }

    @Override
    public String getDescription()
    {
        return "Trajectories: renders where your projectiles (bow, pearls, etc) are gonna land";
    }
}
