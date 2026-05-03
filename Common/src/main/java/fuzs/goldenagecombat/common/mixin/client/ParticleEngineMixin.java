package fuzs.goldenagecombat.common.mixin.client;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.config.ServerConfig;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
abstract class ParticleEngineMixin {

    @Inject(method = "makeParticle", at = @At("HEAD"), cancellable = true)
    private <T extends ParticleOptions> void makeParticle(T options, double x, double y, double z, double xa, double ya, double za, CallbackInfoReturnable<Particle> callback) {
        if (GoldenAgeCombat.CONFIG.get(ServerConfig.class).canceledParticles.contains(options.getType())) {
            callback.setReturnValue(null);
        }
    }
}
