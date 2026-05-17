package sh.idktheflag.idkhack.mixin;


import sh.idktheflag.idkhack.api.event.events.LivingEvent;
import sh.idktheflag.idkhack.api.event.events.move.TravelEvent;
import sh.idktheflag.idkhack.api.event.events.player.ReachEvent;
import sh.idktheflag.idkhack.api.management.RotationManager;
import sh.idktheflag.idkhack.api.utils.ducks.ILivingEntity;
import sh.idktheflag.idkhack.api.utils.players.rotation.Rotation;
import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import sh.idktheflag.idkhack.impl.features.modules.movement.BoatFly;
import sh.idktheflag.idkhack.impl.features.modules.player.Tweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.ParrotEntityModel;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IMinecraft
{
    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract double getEntityInteractionRange();

    @Shadow
    protected abstract boolean canChangeIntoPose(EntityPose pose);

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world)
    {
        super(entityType, world);
    }

    // TODO: port to 1.21.11 - tickNewAi removed, head rotation handling changed
    /*
    @Inject(method = "tickNewAi", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;headYaw:F"))
    public void updateHeadRotation(CallbackInfo ci) { ... }
    */

    @Inject(method = "getBlockInteractionRange", at = @At(value = "HEAD"), cancellable = true)
    private void doReachDistance(CallbackInfoReturnable<Double> cir)
    {
        // TODO: port to 1.21.11 - ReachEvent disabled
    }

    @Inject(method = "getEntityInteractionRange", at = @At(value = "HEAD"), cancellable = true)
    private void doEntityReachDistance(CallbackInfoReturnable<Double> cir)
    {
        // TODO: port to 1.21.11 - ReachEvent disabled
    }

    @Inject(method = "travel", at = @At(value = "HEAD"), cancellable = true)
    private void hookTravelHead(Vec3d movementInput, CallbackInfo ci)
    {
        TravelEvent.Pre event = new TravelEvent.Pre(movementInput);
        event.post();
        if (event.isCancelled())
        {
//            move(MovementType.SELF, getVelocity());
            ci.cancel();
        }
    }


    @Redirect(
            method = {"updatePose"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;canChangeIntoPose(Lnet/minecraft/entity/EntityPose;)Z",
                    ordinal = 0
            )
    )
    private boolean canEnterPose1(PlayerEntity instance, EntityPose pose)
    {
        return (instance == mc.player && Tweaks.INSTANCE.isEnabled() && Tweaks.INSTANCE.crouch.getValue() && mc.player.isSneaking() && !mc.player.isCrawling()) || this.canChangeIntoPose(pose);
    }

    @Redirect(
            method = {"updatePose"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;canChangeIntoPose(Lnet/minecraft/entity/EntityPose;)Z",
                    ordinal = 1
            )
    )
    private boolean canEnterPose2(PlayerEntity instance, EntityPose pose)
    {
        return (instance == mc.player && Tweaks.INSTANCE.crouch.getValue() && mc.player.isSneaking() && !mc.player.isCrawling() &&Tweaks.INSTANCE.isEnabled()) || this.canChangeIntoPose(pose);
    }

    @Inject(method = "travel", at = @At(value = "RETURN"), cancellable = true)
    private void hookTravelTail(Vec3d movementInput, CallbackInfo ci)
    {
        TravelEvent.Post event = new TravelEvent.Post(movementInput);
        event.post();
    }


    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci)
    {
        LivingEvent.Attack event = new LivingEvent.Attack(target);
        event.post();
        if (event.isCancelled())
        {
            ci.cancel();
        }
    }

    @Inject(method = "shouldDismount", at = @At("HEAD"), cancellable = true)
    protected void shouldDismountHook(CallbackInfoReturnable<Boolean> cir)
    {
        if (BoatFly.INSTANCE.isEnabled())
            cir.setReturnValue(false);
    }

}