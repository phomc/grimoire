package dev.phomc.grimoire.enchantment;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompatibilityGraph {
    private final MutableGraph<ResourceLocation> conflictGraph = GraphBuilder.undirected().build();

    public CompatibilityGraph(){
        addConflict(EnchantmentRegistry.FORGE, Enchantments.SILK_TOUCH);
        addConflict(EnchantmentRegistry.DIGGER, Enchantments.BLOCK_FORTUNE);
        addConflict(EnchantmentRegistry.DIGGER, Enchantments.SILK_TOUCH);
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
        Set<ResourceLocation> all = Stream.concat(
                GrimoireItem.of(item).getEnchantmentFeature().enchantments.keySet()
                        .stream().map(GrimoireEnchantment::getIdentifier),
                EnchantmentHelper.getEnchantments(item).keySet()
                        .stream().map(e -> Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.getKey(e)))
        ).filter(e -> conflictGraph.nodes().contains(e)).collect(Collectors.toSet());
        if (all.contains(elem)) return false;
        for(ResourceLocation resourceLocation : all){
            if (conflictGraph.adjacentNodes(resourceLocation).contains(elem)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCompatible(ItemStack item, GrimoireEnchantment e) {
        return isCompatible(item, e.getIdentifier());
    }
}
