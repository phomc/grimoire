package dev.phomc.grimoire.enchantment.weapon;

import dev.phomc.grimoire.enchantment.Enchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

public class ColorShuffleEnchantment extends Enchantment {
    public ColorShuffleEnchantment(ResourceLocation identifier) {
        super(identifier, Rarity.UNCOMMON, Enchantment.Target.WEAPON);
    }

    @Override
    public byte getMaxLevel() {
        return 1;
    }
}

