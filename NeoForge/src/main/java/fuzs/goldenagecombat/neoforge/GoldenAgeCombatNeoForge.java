package fuzs.goldenagecombat.neoforge;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.data.tags.ModDamageTypeTagsProvider;
import fuzs.goldenagecombat.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.common.Mod;

@Mod(GoldenAgeCombat.MOD_ID)
public class GoldenAgeCombatNeoForge {

    public GoldenAgeCombatNeoForge() {
        ModConstructor.construct(GoldenAgeCombat.MOD_ID, GoldenAgeCombat::new);
        DataProviderHelper.registerDataProviders(GoldenAgeCombat.MOD_ID, ModDamageTypeTagsProvider::new);
        DataProviderHelper.registerDataProviders(GoldenAgeCombat.BOOSTED_SHARPNESS_ID,
                PackType.SERVER_DATA,
                ModRegistry.REGISTRY_SET_BUILDER);
    }
}
