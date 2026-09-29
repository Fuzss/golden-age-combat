package fuzs.goldenagecombat.common.data.tags;

import fuzs.goldenagecombat.common.init.ModTags;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v3.tags.AbstractTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.stream.Stream;

public class ModSoundEventTagsProvider extends AbstractTagsProvider<SoundEvent> {

    public ModSoundEventTagsProvider(DataProviderContext context) {
        super(Registries.SOUND_EVENT, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Sounds.SILENT_SOUND_EVENT_TAG)
                .addAll(Stream.of(SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.PLAYER_ATTACK_KNOCKBACK,
                        SoundEvents.PLAYER_ATTACK_NODAMAGE,
                        SoundEvents.PLAYER_ATTACK_STRONG,
                        SoundEvents.PLAYER_ATTACK_WEAK,
                        SoundEvents.PLAYER_ATTACK_SWEEP).map((SoundEvent soundEvent) -> {
                    return BuiltInRegistries.SOUND_EVENT.getResourceKey(soundEvent).orElseThrow();
                }));
    }
}
