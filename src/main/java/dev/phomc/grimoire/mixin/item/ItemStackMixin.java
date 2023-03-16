package dev.phomc.grimoire.mixin.item;

import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import dev.phomc.grimoire.utils.ItemStackUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemHelper {
    @Shadow
    public abstract boolean isEmpty();

    private EnchantmentFeature enchantmentFeature;

    private ItemStack self() {
        return (ItemStack) (Object) this;
    }

    @NotNull
    public EnchantmentFeature getEnchantmentFeature() {
        if (enchantmentFeature == null) {
            enchantmentFeature = new EnchantmentFeature();
            if (!isEmpty()) enchantmentFeature.load(self());
        }
        return enchantmentFeature;
    }

    public void updateDisplay() {
        List<Component> newLore = ItemStackUtils.getLore(self());
        if (newLore == null) {
            newLore = new ArrayList<>();
        } else {
            getEnchantmentFeature().resetLore(newLore);
        }
        getEnchantmentFeature().displayLore(newLore);

        ItemStackUtils.setLore(self(), newLore);
    }

    public void pushChanges() {
        if (isEmpty()) return;
        getEnchantmentFeature().save(self());
        updateDisplay();
    }
}
