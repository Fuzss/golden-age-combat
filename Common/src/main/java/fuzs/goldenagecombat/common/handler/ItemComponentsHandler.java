package fuzs.goldenagecombat.common.handler;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.config.CommonConfig;
import fuzs.goldenagecombat.common.init.ModRegistry;
import fuzs.goldenagecombat.common.util.ToolComponentsHelper;
import fuzs.goldenagecombat.common.util.ToolMaterials;
import fuzs.puzzleslib.common.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.common.api.core.v1.context.ItemComponentsContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class ItemComponentsHandler {

    public static void onRegisterItemComponentPatches(ItemComponentsContext context) {
        if (!GoldenAgeCombat.CONFIG.getHolder(CommonConfig.class).isAvailable()) {
            return;
        }

        if (GoldenAgeCombat.CONFIG.get(CommonConfig.class).noItemDurabilityPenalty) {
            context.registerItemComponentsPatch(Predicates.alwaysTrue(),
                    (DataComponentGetter components, DataComponentMap.Builder builder, HolderLookup.Provider registries, Item item) -> {
                        Weapon weapon = modifyWeaponComponent(components.get(DataComponents.WEAPON));
                        if (weapon != null) {
                            builder.set(DataComponents.WEAPON, weapon);
                        } else {
                            Tool tool = modifyToolComponent(components.get(DataComponents.TOOL));
                            if (tool != null) {
                                builder.set(DataComponents.TOOL, tool);
                            }
                        }
                    });
        }

        if (GoldenAgeCombat.CONFIG.get(CommonConfig.class).allowSwordBlocking) {
            context.registerItemComponentsPatch(Predicates.alwaysTrue(),
                    (DataComponentGetter components, DataComponentMap.Builder builder, HolderLookup.Provider registries, Item item) -> {
                        BlocksAttacks blocksAttacks = createBlocksAttacksComponent(components.get(DataComponents.WEAPON),
                                registries);
                        if (blocksAttacks != null) {
                            builder.set(DataComponents.BLOCKS_ATTACKS, blocksAttacks);
                        }
                    });
        }

        if (GoldenAgeCombat.CONFIG.get(CommonConfig.class).oldAttackDamage) {
            context.registerItemComponentsPatch(Predicates.alwaysTrue(),
                    (DataComponentGetter components, DataComponentMap.Builder builder, HolderLookup.Provider registries, Item item) -> {
                        ItemAttributeModifiers itemAttributeModifiers = modifyAttackDamageAttributeComponent(item,
                                components);
                        if (itemAttributeModifiers != null) {
                            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, itemAttributeModifiers);
                        }
                    });
        }

        if (GoldenAgeCombat.CONFIG.get(CommonConfig.class).removeAttackCooldown) {
            context.registerItemComponentsPatch(Predicates.alwaysTrue(),
                    (DataComponentGetter components, DataComponentMap.Builder builder, HolderLookup.Provider registries, Item item) -> {
                        ItemAttributeModifiers itemAttributeModifiers = modifyAttackSpeedAttributeComponent(components.get(
                                DataComponents.ATTRIBUTE_MODIFIERS));
                        if (itemAttributeModifiers != null) {
                            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, itemAttributeModifiers);
                        }
                    });
        }
    }

    private static @Nullable Weapon modifyWeaponComponent(Weapon weapon) {
        if (weapon != null && weapon.itemDamagePerAttack() == 2) {
            return new Weapon(1, weapon.disableBlockingForSeconds());
        } else {
            return null;
        }
    }

    private static @Nullable Tool modifyToolComponent(Tool tool) {
        if (tool != null && tool.damagePerBlock() == 2) {
            return new Tool(tool.rules(), tool.defaultMiningSpeed(), 1, tool.canDestroyBlocksInCreative());
        } else {
            return null;
        }
    }

    private static @Nullable BlocksAttacks createBlocksAttacksComponent(@Nullable Weapon weapon, HolderLookup.Provider context) {
        if (weapon != null && weapon.itemDamagePerAttack() == 1 && weapon.disableBlockingForSeconds() == 0.0F) {
            return // The original blocking angle should be 360 degrees, but reduce it to be more inline with shield balancing.
                    // The hurt sound does not play when blocking, so use the hurt sound itself as the blocking sound.
                    new BlocksAttacks(0.0F,
                            0.0F,
                            List.of(new BlocksAttacks.DamageReduction(180.0F, Optional.empty(), 0.0F, 0.5F)),
                            new BlocksAttacks.ItemDamageFunction(0.0F, 0.0F, 0.0F),
                            Optional.of(context.lookupOrThrow(Registries.DAMAGE_TYPE)
                                    .getOrThrow(ModRegistry.BYPASSES_SWORD_BLOCK_DAMAGE_TYPE_TAG)),
                            Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GENERIC_HURT)),
                            Optional.empty());
        } else {
            return null;
        }
    }

    private static @Nullable ItemAttributeModifiers modifyAttackDamageAttributeComponent(Item item, DataComponentGetter components) {
        List<ItemAttributeModifiers.Entry> itemAttributeModifiers = components.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.EMPTY).modifiers();
        List<ItemAttributeModifiers.Entry> itemAttributeModifiers2 = setAttributeValue(item,
                itemAttributeModifiers,
                Attributes.ATTACK_DAMAGE,
                Item.BASE_ATTACK_DAMAGE_ID,
                GoldenAgeCombat.CONFIG.get(CommonConfig.class).attackDamageOverrides);
        if (itemAttributeModifiers == itemAttributeModifiers2) {
            OptionalDouble baseAttackDamage = getBaseAttackDamage(components);
            OptionalDouble attackDamageBonus = getAttackDamageBonus(components);
            if (baseAttackDamage.isPresent() && attackDamageBonus.isPresent()) {
                itemAttributeModifiers = setAttributeValue(itemAttributeModifiers,
                        Attributes.ATTACK_DAMAGE,
                        Item.BASE_ATTACK_DAMAGE_ID,
                        baseAttackDamage.getAsDouble() + attackDamageBonus.getAsDouble());
            }
        } else {
            itemAttributeModifiers = itemAttributeModifiers2;
        }

        if (components.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).modifiers()
                != itemAttributeModifiers) {
            return new ItemAttributeModifiers(ImmutableList.copyOf(itemAttributeModifiers));
        } else {
            return null;
        }
    }

    private static @Nullable ItemAttributeModifiers modifyAttackSpeedAttributeComponent(@Nullable ItemAttributeModifiers itemAttributeModifiers) {
        if (itemAttributeModifiers != null) {
            List<ItemAttributeModifiers.Entry> itemAttributes = itemAttributeModifiers.modifiers();
            List<ItemAttributeModifiers.Entry> modifiedItemAttributes = hideAttribute(itemAttributes,
                    Attributes.ATTACK_SPEED);
            if (itemAttributes != modifiedItemAttributes) {
                return new ItemAttributeModifiers(ImmutableList.copyOf(modifiedItemAttributes));
            }
        }

        return null;
    }

    private static List<ItemAttributeModifiers.Entry> hideAttribute(List<ItemAttributeModifiers.Entry> itemAttributeModifiers, Holder<Attribute> holder) {
        for (ItemAttributeModifiers.Entry entry : itemAttributeModifiers) {
            if (entry.attribute().is(holder)) {
                List<ItemAttributeModifiers.Entry> newItemAttributeModifiers = new ArrayList<>(itemAttributeModifiers);
                ListIterator<ItemAttributeModifiers.Entry> iterator = newItemAttributeModifiers.listIterator();
                while (iterator.hasNext()) {
                    ItemAttributeModifiers.Entry newEntry = iterator.next();

                    if (newEntry.attribute().is(holder)) {
                        iterator.set(new ItemAttributeModifiers.Entry(newEntry.attribute(),
                                newEntry.modifier(),
                                newEntry.slot(),
                                ItemAttributeModifiers.Display.hidden()));
                    }
                }

                return newItemAttributeModifiers;
            }
        }

        return itemAttributeModifiers;
    }

    private static OptionalDouble getBaseAttackDamage(DataComponentGetter components) {
        ToolMaterial toolMaterial = ToolMaterials.getToolMaterial(components);
        return toolMaterial != null ? OptionalDouble.of(toolMaterial.attackDamageBonus()) : OptionalDouble.empty();
    }

    private static OptionalDouble getAttackDamageBonus(DataComponentGetter components) {
        if (ToolComponentsHelper.hasComponentsForBlocks(components, BlockTags.SWORD_EFFICIENT)) {
            return OptionalDouble.of(4.0);
        } else if (ToolComponentsHelper.hasComponentsForBlocks(components, BlockTags.MINEABLE_WITH_AXE)) {
            return OptionalDouble.of(3.0);
        } else if (ToolComponentsHelper.hasComponentsForBlocks(components, BlockTags.MINEABLE_WITH_PICKAXE)) {
            return OptionalDouble.of(2.0);
        } else if (ToolComponentsHelper.hasComponentsForBlocks(components, BlockTags.MINEABLE_WITH_SHOVEL)) {
            return OptionalDouble.of(1.0);
        } else if (ToolComponentsHelper.hasComponentsForBlocks(components, BlockTags.MINEABLE_WITH_HOE)) {
            return OptionalDouble.of(0.0);
        } else {
            return OptionalDouble.empty();
        }
    }

    private static List<ItemAttributeModifiers.Entry> setAttributeValue(Item item, List<ItemAttributeModifiers.Entry> itemAttributeModifiers, Holder<Attribute> attribute, Identifier id, ConfigDataSet<Item> attackDamageOverrides) {
        if (attackDamageOverrides.contains(item)) {
            double newValue = attackDamageOverrides.<Double>getOptional(item, 0).orElseThrow();
            return setAttributeValue(itemAttributeModifiers, attribute, id, newValue);
        } else {
            return itemAttributeModifiers;
        }
    }

    private static List<ItemAttributeModifiers.Entry> setAttributeValue(List<ItemAttributeModifiers.Entry> itemAttributeModifiers, Holder<Attribute> attribute, Identifier id, double newValue) {
        itemAttributeModifiers = new ArrayList<>(itemAttributeModifiers);
        AttributeModifier attributeModifier = new AttributeModifier(id,
                newValue,
                AttributeModifier.Operation.ADD_VALUE);
        ItemAttributeModifiers.Entry newEntry = new ItemAttributeModifiers.Entry(attribute,
                attributeModifier,
                EquipmentSlotGroup.MAINHAND);
        ListIterator<ItemAttributeModifiers.Entry> iterator = itemAttributeModifiers.listIterator();
        while (iterator.hasNext()) {
            ItemAttributeModifiers.Entry entry = iterator.next();
            if (entry.slot() == EquipmentSlotGroup.MAINHAND && entry.matches(attribute, id)) {
                iterator.set(newEntry);
                return itemAttributeModifiers;
            }
        }

        itemAttributeModifiers.add(newEntry);
        return itemAttributeModifiers;
    }
}
