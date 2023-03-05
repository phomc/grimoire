package dev.phomc.grimoire.enchantment.weapon;

import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;

import java.util.concurrent.ThreadLocalRandom;

public class ColorShuffleEnchantment extends GrimoireEnchantment {
    public ColorShuffleEnchantment(ResourceLocation identifier) {
        super(identifier, EnchantmentRarity.COMMON, EnchantmentTarget.WEAPON);
    }

    @Override
    public byte getMaxLevel() {
        return 1;
    }

    @Override
    public void onPlayerAttack(Player player, Entity entity, byte level) {
        ((Sheep) entity).setColor(DyeColor.values()[ThreadLocalRandom.current().nextInt(DyeColor.values().length)]);
    }
}
