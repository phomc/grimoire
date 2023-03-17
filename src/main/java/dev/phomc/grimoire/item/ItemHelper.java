package dev.phomc.grimoire.item;

import dev.phomc.grimoire.item.features.CustomItemFeature;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemHelper {
    static ItemHelper of(ItemStack itemStack) {
        if (itemStack == null) throw new IllegalArgumentException("item is null");
        return (ItemHelper) (Object) itemStack;
    }

    @NotNull EnchantmentFeature getEnchantmentFeature();

    @NotNull CustomItemFeature getCustomItemFeature();

    void updateDisplay();

    void saveChanges();
}
