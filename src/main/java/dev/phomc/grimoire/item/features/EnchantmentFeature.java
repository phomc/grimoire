package dev.phomc.grimoire.item.features;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static dev.phomc.grimoire.enchantment.DummyEnchantment.CURSE_DISPLAY_NAME_FORMAT;
import static dev.phomc.grimoire.enchantment.DummyEnchantment.NORMAL_DISPLAY_NAME_FORMAT;

public class EnchantmentFeature extends Feature implements Displayable {
    private Map<GrimoireEnchantment, Integer> enchantments = new LinkedHashMap<>(); // preserve order

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

    public boolean isEmpty() {
        return enchantments.isEmpty();
    }

    public void removeAll() {
        enchantments = new LinkedHashMap<>();
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
        if (enchantments.isEmpty()) {
            reset(itemStack);
            return;
        }
        mergeVanilla(itemStack, map -> map.putAll(enchantments));
    }

    @Override
    public void reset(ItemStack itemStack) {
        mergeVanilla(itemStack, map -> {});
    }

    public void mergeVanilla(ItemStack itemStack, Consumer<Map<Enchantment, Integer>> handler) {
        Map<Enchantment, Integer> map = new LinkedHashMap<>();
        for(Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
            if (entry.getKey() instanceof GrimoireEnchantment) continue;
            map.put(entry.getKey(), entry.getValue());
        }
        handler.accept(map);
        if (map.isEmpty()) {
            resetAll(itemStack);
            return;
        }
        String tag = itemStack.is(Items.ENCHANTED_BOOK) ? EnchantedBookItem.TAG_STORED_ENCHANTMENTS : "Enchantments";
        ListTag listTag = new ListTag();
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            listTag.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(entry.getKey()), entry.getValue()));
        }
        itemStack.getOrCreateTag().put(tag, listTag);
    }

    public void resetAll(ItemStack itemStack) {
        if (itemStack.is(Items.ENCHANTED_BOOK)) {
            itemStack.removeTagKey(EnchantedBookItem.TAG_STORED_ENCHANTMENTS);
        } else {
            itemStack.removeTagKey("Enchantments");
        }
    }

    @Override
    public void displayLore(List<Component> lines) {
        for (Map.Entry<GrimoireEnchantment, Integer> entry : enchantments.entrySet()) {
            lines.add(entry.getKey().getFullname(entry.getValue()));
        }
    }

    @Override
    public void resetLore(List<Component> lines) {
        Set<String> descIds = EnchantmentRegistry.ALL.values().stream()
                .map(Enchantment::getDescriptionId)
                .collect(Collectors.toUnmodifiableSet());
        lines.removeIf(cpn -> {
            return cpn instanceof MutableComponent &&
                    cpn.getContents() instanceof TranslatableContents contents &&
                    descIds.contains(contents.getKey()) &&
                    (cpn.getStyle().equals(NORMAL_DISPLAY_NAME_FORMAT) ||
                            cpn.getStyle().equals(CURSE_DISPLAY_NAME_FORMAT));
        });
    }
}
