package dev.phomc.grimoire.enchantment.tool;

import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class ForgeEnchantment extends GrimoireEnchantment {
    public ForgeEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, EnchantmentRarity.RARE, EnchantmentTarget.PICKAXE);
    }

    public byte getMaxLevel() {
        return 4;
    }

    public boolean shouldSmelt(byte level) {
        if (level == getMaxLevel()) return true;
        return ThreadLocalRandom.current().nextFloat() < 0.25f * level;
    }
}
