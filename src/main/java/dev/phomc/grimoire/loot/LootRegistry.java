package dev.phomc.grimoire.loot;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.item.ItemRegistry;
import dev.phomc.grimoire.item.custom.GemstoneItem;
import dev.phomc.grimoire.utils.DevModeUtils;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LootRegistry {
    public static final Multimap<ResourceLocation, LootModifier> MODIFIERS = HashMultimap.create();

    static {
        registerGemstoneLoot(
                Blocks.DEEPSLATE_COPPER_ORE,
                new Pair<>(ItemRegistry.TOPAZ, 0.3f)
        );
        registerGemstoneLoot(
                Blocks.IRON_ORE,
                new Pair<>(ItemRegistry.TOPAZ, 0.4f)
        );
        registerGemstoneLoot(
                Blocks.DEEPSLATE_IRON_ORE,
                new Pair<>(ItemRegistry.TOPAZ, 0.5f)
        );
        registerGemstoneLoot(
                Blocks.GOLD_ORE,
                new Pair<>(ItemRegistry.TOPAZ, 0.7f)
        );
        registerGemstoneLoot(
                Blocks.DEEPSLATE_GOLD_ORE,
                new Pair<>(ItemRegistry.TOPAZ, 0.8f),
                new Pair<>(ItemRegistry.JADE, 0.5f)
        );
        registerGemstoneLoot(
                Blocks.LAPIS_ORE,
                new Pair<>(ItemRegistry.SAPPHIRE, 0.5f)
        );
        registerGemstoneLoot(
                Blocks.DEEPSLATE_LAPIS_ORE,
                new Pair<>(ItemRegistry.SAPPHIRE, 0.6f),
                new Pair<>(ItemRegistry.JADE, 0.4f)
        );
        registerGemstoneLoot(
                Blocks.EMERALD_ORE,
                new Pair<>(ItemRegistry.SAPPHIRE, 0.4f),
                new Pair<>(ItemRegistry.JADE, 0.6f)
        );
        registerGemstoneLoot(
                Blocks.DEEPSLATE_EMERALD_ORE,
                new Pair<>(ItemRegistry.SAPPHIRE, 0.5f),
                new Pair<>(ItemRegistry.JADE, 0.7f)
        );
        registerGemstoneLoot(
                Blocks.DIAMOND_ORE,
                new Pair<>(ItemRegistry.JADE, 0.5f),
                new Pair<>(ItemRegistry.SAPPHIRE, 0.6f),
                new Pair<>(ItemRegistry.MUSGRAVITE, 0.3f)
        );
        registerGemstoneLoot(
                Blocks.DEEPSLATE_DIAMOND_ORE,
                new Pair<>(ItemRegistry.TOPAZ, 0.6f),
                new Pair<>(ItemRegistry.JADE, 0.7f),
                new Pair<>(ItemRegistry.SAPPHIRE, 0.8f),
                new Pair<>(ItemRegistry.MUSGRAVITE, 0.5f)
        );
        registerGemstoneLoot(
                Blocks.ANCIENT_DEBRIS,
                new Pair<>(ItemRegistry.TOPAZ, 0.7f),
                new Pair<>(ItemRegistry.JADE, 0.7f),
                new Pair<>(ItemRegistry.SAPPHIRE, 0.8f),
                new Pair<>(ItemRegistry.MUSGRAVITE, null)
        );
    }

    @SafeVarargs
    public static void registerGemstoneLoot(Block block, Pair<GemstoneItem, Float>... pairs) {
        DevModeUtils.runInDev(new Runnable() {
            @Override
            public void run() {
                Grimoire.LOGGER.info("{} contains {}", block.getName().getString(), Arrays.stream(pairs)
                        .map(p -> p.getSecond() + " " + p.getFirst().getType().getId())
                        .collect(Collectors.joining(", ")));
            }
        });
        MODIFIERS.put(block.getLootTable(), tableBuilder -> {
            LootPool.Builder pool = LootPool.lootPool()
                    .with(AlternativesEntry.alternatives(
                            EmptyLootItem.emptyItem()
                                    .when(MatchTool.toolMatches(
                                            ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.ANY))
                                    )),
                            SequentialEntry.sequential(
                                    Arrays.stream(pairs).map((Function<Pair<GemstoneItem, Float>, LootPoolEntryContainer.Builder<?>>) pair -> {
                                        LootPoolSingletonContainer.Builder<?> b = LootItem.lootTableItem(pair.getFirst())
                                                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                                                .apply(ApplyExplosionDecay.explosionDecay());
                                        if (pair.getSecond() != null) {
                                            b.when(LootItemRandomChanceCondition.randomChance(pair.getSecond()));
                                        }
                                        return b;
                                    }).toArray(LootPoolEntryContainer.Builder[]::new)
                            )
                    ).build())
                    .setRolls(ConstantValue.exactly(1));

            tableBuilder.pool(pool.build());
        });
    }
}
