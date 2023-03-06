package dev.phomc.grimoire.enchantment.melee;

import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

import java.util.concurrent.ThreadLocalRandom;

public class ColorShuffleEnchantment extends GrimoireEnchantment {
    public ColorShuffleEnchantment(ResourceLocation identifier) {
        super(identifier, EnchantmentRarity.COMMON, EnchantmentTarget.MELEE);
    }

    @Override
    public void onAttack(AttackRecord attackRecord, byte level) {
        if (attackRecord.victim() instanceof Sheep) {
            ((Sheep) attackRecord.victim()).setColor(DyeColor.values()[ThreadLocalRandom.current().nextInt(DyeColor.values().length)]);
        }
    }
}
