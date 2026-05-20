package sh.idktheflag.idk.mixin;

import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.world.chunk.WorldChunk;
import sh.idktheflag.idk.api.event.events.world.ChunkDataEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;
import sh.idktheflag.idk.api.utils.ducks.IClientPlayNetworkHandler;
import sh.idktheflag.idk.impl.features.modules.player.Velocity;
import sh.idktheflag.idk.mixin.accessor.IClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler implements IClientPlayNetworkHandler {

    @Shadow
    public abstract ClientConnection getConnection();

    @Override
    public void sendQuietPacket(final Packet<?> packet) {
        ((IClientConnection) getConnection()).sendQuietPacket(packet, null, true);
    }

    @Inject(method = "onChunkData", at = @At("TAIL"))
    private void hookOnChunkData(ChunkDataS2CPacket packet, CallbackInfo ci)
    {
        WorldChunk chunk = ((ClientPlayNetworkHandler) (Object) this).getWorld().getChunk(packet.getChunkX(), packet.getChunkZ());
        if (chunk != null)
        {
            new ChunkDataEvent(chunk, false).post();
        }
    }

    private boolean ignoreExplosion = false;

    @Inject(method = "onExplosion", at = @At("HEAD"), cancellable = true)
    private void hookOnExplosion(ExplosionS2CPacket packet, CallbackInfo ci)
    {
        if (ignoreExplosion) return;
        if (!Velocity.INSTANCE.isEnabled()) return;
        if (!"Vanilla".equals(Velocity.INSTANCE.mode.getValue())) return;
        if (packet.playerKnockback().isEmpty()) return;

        float h = Velocity.INSTANCE.horizontal.getValue().floatValue() / 100.0f;
        float v = Velocity.INSTANCE.vertical.getValue().floatValue() / 100.0f;

        ignoreExplosion = true;
        if (h == 0 && v == 0)
        {
            // Cancel knockback entirely by replacing with empty Optional
            ((ClientPlayNetworkHandler) (Object) this).onExplosion(new ExplosionS2CPacket(
                    packet.center(), packet.radius(), packet.blockCount(),
                    Optional.empty(),
                    packet.explosionParticle(), packet.explosionSound(), packet.blockParticles()
            ));
            ignoreExplosion = false;
            ci.cancel();
            return;
        }

        Vec3d knockback = packet.playerKnockback().get();
        Vec3d scaled = new Vec3d(knockback.x * h, knockback.y * v, knockback.z * h);
        ((ClientPlayNetworkHandler) (Object) this).onExplosion(new ExplosionS2CPacket(
                packet.center(), packet.radius(), packet.blockCount(),
                Optional.of(scaled),
                packet.explosionParticle(), packet.explosionSound(), packet.blockParticles()
        ));
        ignoreExplosion = false;
        ci.cancel();
    }
}
