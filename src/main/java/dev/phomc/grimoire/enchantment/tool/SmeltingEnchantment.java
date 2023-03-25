package dev.phomc.grimoire.enchantment.tool;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SmeltingEnchantment extends GrimoireEnchantment {
    public SmeltingEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.UNCOMMON, EnchantmentTarget.DIGGER);

        createProperty("chance", (DecimalProperty) level -> level * 0.25);
    }

    public int getMaxLevel() {
        return 4;
    }

    public boolean shouldSmelt(int level) {
        return requireDecimalProperty("chance").randomize(level);
    }
}
