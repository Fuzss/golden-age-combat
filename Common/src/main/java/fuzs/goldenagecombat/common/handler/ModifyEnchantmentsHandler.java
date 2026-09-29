package fuzs.goldenagecombat.common.handler;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.config.CommonConfig;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;

public final class ModifyEnchantmentsHandler {

    private ModifyEnchantmentsHandler() {
        // NO-OP
    }

    public static boolean modifyEnchantment(ResourceKey<Enchantment> key, Enchantment.Builder builder, RegistryOps.RegistryInfoLookup lookup) {
        if (key == Enchantments.SHARPNESS) {
            return modifySharpness(builder);
        } else {
            return false;
        }
    }

    private static boolean modifySharpness(Enchantment.Builder builder) {
        if (!GoldenAgeCombat.CONFIG.get(CommonConfig.class).boostSharpness) {
            return false;
        }

        // Boost sharpness damage to 1.25 per level instead of the vanilla 1.0 plus 0.5 per level above first.
        builder.getEffectsList(EnchantmentEffectComponents.DAMAGE).clear();
        builder.withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(1.25F)));
        return true;
    }
}
