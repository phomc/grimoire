package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import dev.phomc.grimoire.utils.ItemStackUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements GrimoireItem {
    @Shadow public abstract void enchant(Enchantment enchantment, int i);

    @Unique
    private EnchantmentFeature enchantmentFeature;

    private ItemStack self(){
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

    public void updateLore() {
        List<Component> newLore = new ArrayList<>();
        List<Component> oldLore = ItemStackUtils.getLore(self());
        if (oldLore != null) {
            // remove features
            enchantmentFeature.hideLore(oldLore);
        }

        // re-generate new lore
        // 1. enchantment first
        enchantmentFeature.displayLore(newLore);
        // 2. old lore with features stripped
        if (oldLore != null) newLore.addAll(oldLore);
        ItemStackUtils.setLore(self(), newLore);
    }

    public void pushChanges() {
        enchantmentFeature.save(self());
        updateLore();
    }
}
