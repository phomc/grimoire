package dev.phomc.grimoire.item;

import dev.phomc.grimoire.item.features.EnchantmentFeature;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface GrimoireItem {
    static GrimoireItem of(ItemStack itemStack) {
        return (GrimoireItem) (Object) itemStack;
    }

    @NotNull EnchantmentFeature getEnchantmentFeature();

    void updateLore();

    void pushChanges();
}
