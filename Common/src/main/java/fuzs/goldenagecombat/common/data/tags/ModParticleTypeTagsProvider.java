package fuzs.goldenagecombat.common.data.tags;

import fuzs.goldenagecombat.common.init.ModTags;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v3.tags.AbstractTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;

import java.util.stream.Stream;

public class ModParticleTypeTagsProvider extends AbstractTagsProvider<ParticleType<?>> {

    public ModParticleTypeTagsProvider(DataProviderContext context) {
        super(Registries.PARTICLE_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.ParticleTypes.INVISIBLE_PARTICLE_TYPE_TAG)
                .addAll(Stream.of(ParticleTypes.DAMAGE_INDICATOR, ParticleTypes.SWEEP_ATTACK)
                        .map((SimpleParticleType particleType) -> {
                            return BuiltInRegistries.PARTICLE_TYPE.getResourceKey(particleType).orElseThrow();
                        }));
    }
}
