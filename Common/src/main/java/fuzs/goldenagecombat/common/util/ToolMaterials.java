package fuzs.goldenagecombat.common.util;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Repairable;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ToolMaterials {
    private static final Map<ToolMaterialKey, ToolMaterial> TOOL_MATERIALS = new ConcurrentHashMap<>();

    public static void registerToolMaterial(ToolMaterial toolMaterial) {
        TOOL_MATERIALS.put(ToolMaterialKey.of(toolMaterial), toolMaterial);
    }

    @Nullable
    public static ToolMaterial getToolMaterial(DataComponentGetter components) {
        ToolMaterialKey key = ToolMaterialKey.of(components);
        if (key != null) {
            return TOOL_MATERIALS.get(key);
        } else {
            return null;
        }
    }

    private record ToolMaterialKey(int durability, int enchantmentValue, TagKey<Item> repairItems) {

        public static ToolMaterialKey of(ToolMaterial toolMaterial) {
            return new ToolMaterialKey(toolMaterial.durability(),
                    toolMaterial.enchantmentValue(),
                    toolMaterial.repairItems());
        }

        public static ToolMaterials.@Nullable ToolMaterialKey of(DataComponentGetter components) {
            Repairable repairable = components.get(DataComponents.REPAIRABLE);
            if (repairable != null) {
                Optional<TagKey<Item>> optional = repairable.items().unwrapKey();
                if (optional.isPresent()) {
                    Integer maxDamage = components.get(DataComponents.MAX_DAMAGE);
                    Enchantable enchantable = components.get(DataComponents.ENCHANTABLE);
                    if (maxDamage != null && enchantable != null) {
                        return new ToolMaterialKey(maxDamage, enchantable.value(), optional.get());
                    }
                }
            }

            return null;
        }
    }
}
