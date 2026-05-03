package fuzs.goldenagecombat.util;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Also used in Combat Nouveau mod.
 */
public final class ToolComponentsHelper {

    private ToolComponentsHelper() {
        // NO-OP
    }

    public static boolean isTool(DataComponentGetter components) {
        return isToolOrWeapon(components, 1, 2);
    }

    public static boolean isWeapon(DataComponentGetter components) {
        return isToolOrWeapon(components, 2, 1);
    }

    private static boolean isToolOrWeapon(DataComponentGetter components, int damagePerBlock, int itemDamagePerAttack) {
        Tool tool = components.get(DataComponents.TOOL);
        if (tool != null && tool.damagePerBlock() == damagePerBlock
                && tool.canDestroyBlocksInCreative() == itemDamagePerAttack > damagePerBlock) {
            Weapon weapon = components.get(DataComponents.WEAPON);
            return weapon != null && weapon.itemDamagePerAttack() == itemDamagePerAttack;
        }

        return false;
    }

    public static boolean hasComponentsForBlocks(DataComponentGetter components, TagKey<Block> tagKey) {
        return getToolForBlocks(components, tagKey) != null;
    }

    @Nullable
    public static Tool getToolForBlocks(DataComponentGetter components, TagKey<Block> tagKey) {
        Tool tool = components.get(DataComponents.TOOL);
        if (tool != null && isToolForBlocks(tool, tagKey)) {
            return tool;
        } else {
            return null;
        }
    }

    private static boolean isToolForBlocks(Tool tool, TagKey<Block> tagKey) {
        for (Tool.Rule rule : tool.rules()) {
            if (isRuleForBlocks(rule, tagKey)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isRuleForBlocks(Tool.Rule rule, TagKey<Block> tagKey) {
        if (rule.correctForDrops().filter(Predicate.not(Boolean::booleanValue)).isEmpty()) {
            return rule.blocks().unwrapKey().filter((TagKey<Block> tagKeyX) -> tagKeyX == tagKey).isPresent();
        } else {
            return false;
        }
    }
}
