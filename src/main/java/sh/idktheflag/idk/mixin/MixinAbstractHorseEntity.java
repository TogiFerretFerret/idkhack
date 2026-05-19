package sh.idktheflag.idk.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

// isSaddled() was removed in 1.21.11 - saddle is now an equipment slot
// TODO: find new injection point for EntityControl saddle bypass
@Mixin(AbstractHorseEntity.class)
public abstract class MixinAbstractHorseEntity extends AnimalEntity {
    protected MixinAbstractHorseEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }
}
