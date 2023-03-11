package dev.phomc.grimoire.enchantment;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class CompatibilityGraph {
    private final MutableGraph<ResourceLocation> conflictGraph = GraphBuilder.undirected().build();

    public CompatibilityGraph(){
        addConflict(EnchantmentRegistry.SMELTING, Enchantments.SILK_TOUCH);
        addConflict(EnchantmentRegistry.DIGGER, Enchantments.BLOCK_FORTUNE);
        addConflict(EnchantmentRegistry.DIGGER, Enchantments.SILK_TOUCH);
        addConflict(EnchantmentRegistry.TUNNEL, Enchantments.BLOCK_FORTUNE);
        addConflict(EnchantmentRegistry.TUNNEL, EnchantmentRegistry.DIGGER);
        addConflict(EnchantmentRegistry.RIFT, Enchantments.INFINITY_ARROWS);
        addConflict(EnchantmentRegistry.RIFT, Enchantments.MULTISHOT);
        addConflict(EnchantmentRegistry.RIFT, Enchantments.RIPTIDE);
        addConflict(EnchantmentRegistry.EXPLOSIVE, Enchantments.MULTISHOT);
        addConflict(EnchantmentRegistry.THUNDER, Enchantments.CHANNELING);
    }

    public void addConflict(ResourceLocation a, ResourceLocation b) {
        conflictGraph.putEdge(a, b);
    }

    public void addConflict(GrimoireEnchantment a, GrimoireEnchantment b) {
        addConflict(a.getIdentifier(), b.getIdentifier());
    }

    public void addConflict(GrimoireEnchantment a, Enchantment b) {
        addConflict(a.getIdentifier(), Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.getKey(b)));
    }

    public boolean isCompatible(ItemStack item, ResourceLocation elem) {
        Set<ResourceLocation> all = EnchantmentHelper.getEnchantments(item)
                .keySet().stream()
                .map(e -> Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.getKey(e)))
                .collect(Collectors.toSet());
        if (conflictGraph.nodes().contains(elem)) {
            for (ResourceLocation resourceLocation : conflictGraph.adjacentNodes(elem)) {
                if (all.contains(resourceLocation)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isCompatible(ItemStack item, GrimoireEnchantment e) {
        return isCompatible(item, e.getIdentifier());
    }

    public boolean isCompatible(GrimoireEnchantment a, Enchantment b) {
        if (conflictGraph.nodes().contains(a.getIdentifier())) {
            return !conflictGraph.adjacentNodes(a.getIdentifier()).contains(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.getKey(b)));
        }
        return true;
    }
}
