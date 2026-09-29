package fuzs.goldenagecombat.common.init;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class ModTags {
    public static class DamageTypes {
        public static final TagKey<DamageType> BYPASSES_SWORD_BLOCK_DAMAGE_TYPE_TAG = register("bypasses_sword_block");

        private static TagKey<DamageType> register(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, GoldenAgeCombat.id(name));
        }
    }

    public static class ParticleTypes {
        public static final TagKey<ParticleType<?>> INVISIBLE_PARTICLE_TYPE_TAG = register("invisible");

        private static TagKey<ParticleType<?>> register(String name) {
            return TagKey.create(Registries.PARTICLE_TYPE, GoldenAgeCombat.id(name));
        }
    }

    public static class Sounds {
        public static final TagKey<SoundEvent> SILENT_SOUND_EVENT_TAG = register("silent");

        private static TagKey<SoundEvent> register(String name) {
            return TagKey.create(Registries.SOUND_EVENT, GoldenAgeCombat.id(name));
        }
    }
}
