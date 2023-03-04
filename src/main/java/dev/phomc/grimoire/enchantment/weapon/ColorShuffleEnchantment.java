package dev.phomc.grimoire.enchantment.weapon;

import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class ColorShuffleEnchantment extends GrimoireEnchantment {
    public ColorShuffleEnchantment(ResourceLocation identifier) {
        super(identifier, EnchantmentRarity.UNCOMMON, EnchantmentTarget.WEAPON);
    }

    @Override
    public byte getMaxLevel() {
        return 1;
    }

    @Override
    public void doPostAttack(LivingEntity livingEntity, Entity entity, int level) {
        if (livingEntity instanceof Player && entity instanceof Sheep) {
            ItemStack item = livingEntity.getMainHandItem();
            if (item.isEmpty()) return;
            if (GrimoireItem.of(item).getEnchantmentFeature().getEnchantment(EnchantmentRegistry.COLOR_SHUFFLE) > 0) {
                ((Sheep) entity).setColor(DyeColor.values()[ThreadLocalRandom.current().nextInt(DyeColor.values().length)]);
            }
        }
    }
}
