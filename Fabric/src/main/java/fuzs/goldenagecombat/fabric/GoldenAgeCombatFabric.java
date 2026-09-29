package fuzs.goldenagecombat.fabric;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.handler.ModifyEnchantmentsHandler;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.item.v1.ResourceSource;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class GoldenAgeCombatFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(GoldenAgeCombat.MOD_ID, GoldenAgeCombat::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        EnchantmentEvents.MODIFY_WITH_LOOKUP.register((ResourceKey<Enchantment> key, Enchantment.Builder builder, ResourceSource source, RegistryOps.RegistryInfoLookup registries) -> ModifyEnchantmentsHandler.modifyEnchantment(
                key,
                builder,
                registries));
    }
}
