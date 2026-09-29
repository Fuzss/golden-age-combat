package fuzs.goldenagecombat.common.mixin.client;

import fuzs.goldenagecombat.common.init.ModTags;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
abstract class ParticleEngineMixin {

    @Inject(method = "makeParticle", at = @At("HEAD"), cancellable = true)
    private <T extends ParticleOptions> void makeParticle(T options, double x, double y, double z, double xa, double ya, double za, CallbackInfoReturnable<@Nullable Particle> callback) {
        if (BuiltInRegistries.PARTICLE_TYPE.wrapAsHolder(options.getType())
                .is(ModTags.ParticleTypes.INVISIBLE_PARTICLE_TYPE_TAG)) {
            callback.setReturnValue(null);
        }
    }
}
