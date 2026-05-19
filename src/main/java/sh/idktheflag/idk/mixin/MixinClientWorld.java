package sh.idktheflag.idk.mixin;

import sh.idktheflag.idk.api.event.events.world.EntityEvent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {

    @Shadow
    @Nullable
    public abstract Entity getEntityById(int id);

    @Inject(method = "addEntity", at = @At(value = "RETURN"))
    private void hookAddEntity(Entity entity, CallbackInfo ci)
    {
        new EntityEvent.Add(entity).post();
    }

    @Inject(method = "removeEntity", at = @At(value = "HEAD"))
    private void hookRemoveEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci)
    {
        Entity entity = getEntityById(entityId);
        if (entity == null)
        {
            return;
        }
        new EntityEvent.Remove(entity, removalReason).post();
    }


    // getSkyColor removed in 1.21.11 - CustomSky now hooks SkyRendering.updateRenderState via MixinSkyRendering

}