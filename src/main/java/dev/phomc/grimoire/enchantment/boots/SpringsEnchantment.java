package dev.phomc.grimoire.enchantment.boots;

import dev.phomc.grimoire.enchantment.Enchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

public class SpringsEnchantment extends Enchantment {

    public SpringsEnchantment(ResourceLocation identifier) {
        super(identifier, Rarity.UNCOMMON, Enchantment.Target.BOOTS);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
