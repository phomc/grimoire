package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import dev.phomc.grimoire.item.features.LoreFeature;
import dev.phomc.grimoire.utils.ItemStackUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin implements GrimoireItem {
    private EnchantmentFeature enchantmentFeature;
    private LoreFeature loreFeature;

    private ItemStack self() {
        return (ItemStack) (Object) this;
    }

    @NotNull
    public EnchantmentFeature getEnchantmentFeature() {
        if (enchantmentFeature == null) {
            enchantmentFeature = new EnchantmentFeature();
            enchantmentFeature.load(self());
        }
        return enchantmentFeature;
    }

    @NotNull
    public LoreFeature getLoreFeature() {
        if (loreFeature == null) {
            loreFeature = new LoreFeature();
            loreFeature.load(self());
        }
        return loreFeature;
    }

    public void updateDisplay() {
        List<Component> newLore = new ArrayList<>();
        // 1. enchantment first
        getEnchantmentFeature().displayLore(newLore);
        // 2. then lore
        getLoreFeature().displayLore(newLore);

        ItemStackUtils.setLore(self(), newLore);

        // enchantment effect
        if (getEnchantmentFeature().enchantments.isEmpty()) {
            ItemStackUtils.removeEnchantment(self(), EnchantmentRegistry.DUMMY);
        } else {
            self().enchant(EnchantmentRegistry.DUMMY, 1);
        }
    }

    public void pushChanges() {
        getEnchantmentFeature().save(self());
        getLoreFeature().save(self());
        updateDisplay();
    }
}
