package sh.idktheflag.idk.impl.features.modules.ghost;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.LivingEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.FriendManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.Timer;
import sh.idktheflag.idk.api.utils.math.MathUtil;
import sh.idktheflag.idk.api.utils.world.CrystalUtil;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.mixin.accessor.IMinecraftClient;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;


public class FastMechs extends Module {
    public static FastMechs INSTANCE;

    public FastMechs() {
        super("FastMechs", Category.Ghost);
        INSTANCE = this;
    }

    public static boolean FAST_CRYSTAL = false;
    Timer breakTimer = new Timer(80);
    Timer fastTimer = new Timer(200);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null) {
            FAST_CRYSTAL = false;
            return;
        }


        if (mc.options.useKey.isPressed()) {
            if (fastTimer.isPassed()) {
                FAST_CRYSTAL = true;
            }
        } else {
            fastTimer.resetDelay();
            FAST_CRYSTAL = false;
        }


    }

    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.VanillaTick event) {
        if (FAST_CRYSTAL && mc.options.useKey.isPressed()) {
            if (mc.crosshairTarget instanceof EntityHitResult hit) {


                if (breakTimer.isPassed()) {
                    if (hit.getEntity() instanceof EndCrystalEntity crystal) {
                        mc.interactionManager.attackEntity(mc.player, crystal);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        breakTimer.setDelay((long) MathUtil.random(60, 120));
                        breakTimer.resetDelay();
                    }
                }
            }
        }
    }

    public int getWantedDelay(ItemStack itemStack) {
        if (itemStack.getItem() == Items.GLOWSTONE) {

            if (!mc.player.isSneaking()) {
                HitResult hit = mc.crosshairTarget;
                if (hit == null)
                    return -1;

                if (mc.crosshairTarget.getType() != HitResult.Type.BLOCK)
                    return -1;


                BlockHitResult hitResult = (BlockHitResult) hit;
                BlockState state = mc.world.getBlockState(hitResult.getBlockPos());
                if (state.getBlock() == Blocks.RESPAWN_ANCHOR) {
                    if (state.get(Properties.CHARGES) == 0)
                        return 0;
                } else {
                    return 1;

                }
            }
            return -1;
        }

        if (itemStack.getItem() == Items.TOTEM_OF_UNDYING || itemStack.getItem() == Items.GLOWSTONE_DUST) return 0;

        if (itemStack.getItem() == Items.END_CRYSTAL && FAST_CRYSTAL) return 0;

        if (itemStack.isEmpty()) return 0;
        return -1;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) return;

    }


    @Override
    public String getDescription() {
        return "FastMechs: makes u able to anchor and safe anchor fast like marlow";
    }
}