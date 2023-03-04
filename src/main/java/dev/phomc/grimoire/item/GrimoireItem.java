package dev.phomc.grimoire.item;

import dev.phomc.grimoire.item.features.EnchantmentFeature;
import dev.phomc.grimoire.item.features.LoreFeature;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface GrimoireItem {
    static GrimoireItem of(ItemStack itemStack) {
        return (GrimoireItem) (Object) itemStack;
    }

    @NotNull EnchantmentFeature getEnchantmentFeature();

    @NotNull LoreFeature getLoreFeature();

    void updateDisplay();

    void pushChanges();
}
