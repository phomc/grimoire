package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import dev.phomc.grimoire.item.features.LoreFeature;
import dev.phomc.grimoire.utils.ItemStackUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements GrimoireItem {
    @Unique
    private EnchantmentFeature enchantmentFeature;

    @Unique
    private LoreFeature loreFeature;

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
        enchantmentFeature.displayLore(newLore);
        // 2. then lore
        loreFeature.displayLore(newLore);

        ItemStackUtils.setLore(self(), newLore);
    }

    public void pushChanges() {
        enchantmentFeature.save(self());
        loreFeature.save(self());
        updateDisplay();
    }
}
