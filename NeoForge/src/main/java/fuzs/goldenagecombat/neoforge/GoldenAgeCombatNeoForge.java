package fuzs.goldenagecombat.neoforge;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.data.tags.ModDamageTypeTagsProvider;
import fuzs.goldenagecombat.common.data.tags.ModParticleTypeTagsProvider;
import fuzs.goldenagecombat.common.data.tags.ModSoundEventTagsProvider;
import fuzs.goldenagecombat.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.common.Mod;

@Mod(GoldenAgeCombat.MOD_ID)
public class GoldenAgeCombatNeoForge {

    public GoldenAgeCombatNeoForge() {
        ModConstructor.construct(GoldenAgeCombat.MOD_ID, GoldenAgeCombat::new);
        DataProviderBuilder.of(GoldenAgeCombat.MOD_ID)
                .addProvider(ModDamageTypeTagsProvider::new,
                        ModParticleTypeTagsProvider::new,
                        ModSoundEventTagsProvider::new);
        DataProviderBuilder.ofBuiltIn(GoldenAgeCombat.BOOSTED_SHARPNESS_ID, PackType.SERVER_DATA)
                .addWorldBootstrap(Registries.ENCHANTMENT, ModRegistry::bootstrapEnchantments);
    }
}
