package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import dev.phomc.grimoire.item.features.LoreFeature;
import dev.phomc.grimoire.utils.ItemStackUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements GrimoireItem {
    @Shadow
    public abstract boolean isEmpty();

    private EnchantmentFeature enchantmentFeature;
    private LoreFeature loreFeature;

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

    @NotNull
    public LoreFeature getLoreFeature() {
        if (loreFeature == null) {
            loreFeature = new LoreFeature();
            if (!isEmpty()) loreFeature.load(self());
        }
        return loreFeature;
    }

    public void updateDisplay() {
        List<Component> newLore = new ArrayList<>();
        getEnchantmentFeature().displayLore(newLore);
        getLoreFeature().displayLore(newLore);

        ItemStackUtils.setLore(self(), newLore);
    }

    public void pushChanges() {
        if (isEmpty()) return;
        getLoreFeature().save(self());
        getEnchantmentFeature().save(self());
        updateDisplay();
    }
}
