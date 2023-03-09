package dev.phomc.grimoire.item.features;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class EnchantmentFeature extends ItemFeature implements Displayable {
    private final Map<GrimoireEnchantment, Integer> enchantments = new LinkedHashMap<>(); // preserve order

    public int getEnchantment(@Nullable ItemStack itemStack, GrimoireEnchantment enchantment) {
        if (enchantment.canEnchant(itemStack) && EnchantmentRegistry.COMPATIBILITY_GRAPH.isCompatible(itemStack, enchantment)) {
            return enchantments.getOrDefault(enchantment, 0);
        }
        return 0;
    }

    public void iterateEnchantments(@Nullable ItemStack itemStack, BiConsumer<GrimoireEnchantment, Integer> consumer) {
        if (itemStack == null || itemStack.isEmpty()) return;
        enchantments.keySet().forEach(e -> consumer.accept(e, getEnchantment(itemStack, e)));
    }

    public void setEnchantment(GrimoireEnchantment enchantment, int lv) {
        enchantments.put(enchantment, lv);
    }

    public void removeEnchantment(GrimoireEnchantment enchantment) {
        enchantments.remove(enchantment);
    }

    @Override
    public void load(ItemStack itemStack) {
        for(Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
            if (entry.getKey() instanceof GrimoireEnchantment) {
                enchantments.put((GrimoireEnchantment) entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void save(ItemStack itemStack) {
        if (enchantments.isEmpty()) return;
        Map<Enchantment, Integer> map = new LinkedHashMap<>();
        for(Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
            if (entry.getKey() instanceof GrimoireEnchantment) continue;
            map.put(entry.getKey(), entry.getValue());
        }
        map.putAll(enchantments);
        EnchantmentHelper.setEnchantments(map, itemStack);
    }

    @Override
    public void reset(ItemStack itemStack) {
        if (enchantments.isEmpty()) return;
        Map<Enchantment, Integer> map = new LinkedHashMap<>();
        for(Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
            if (entry.getKey() instanceof GrimoireEnchantment) continue;
            map.put(entry.getKey(), entry.getValue());
        }
        EnchantmentHelper.setEnchantments(map, itemStack);
    }

    public void resetAll(ItemStack itemStack) {
        itemStack.removeTagKey("Enchantments");
    }

    @Override
    public void displayLore(List<Component> lines) {
        for (Map.Entry<GrimoireEnchantment, Integer> entry : enchantments.entrySet()) {
            lines.add(entry.getKey().getFullname(entry.getValue()));
        }
    }
}
