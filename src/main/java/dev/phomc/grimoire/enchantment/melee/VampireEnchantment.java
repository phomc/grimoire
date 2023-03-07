package dev.phomc.grimoire.enchantment.melee;

import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class VampireEnchantment extends GrimoireEnchantment {
    private static final float[] CHANCE = new float[]{0.2f, 0.25f, 0.3f, 0.3f, 0.3f};
    private static final float[] TRANSFER_RATE = new float[]{0.1f, 0.15f, 0.2f, 0.3f, 0.4f};

    public VampireEnchantment(ResourceLocation identifier) {
        super(identifier, EnchantmentRarity.RARE, EnchantmentTarget.MELEE);
    }

    @Override
    public byte getMaxLevel() {
        return (byte) CHANCE.length;
    }

    @Override
    public void onAttack(AttackRecord attackRecord, byte level) {
        int index = Math.min(level, getMaxLevel()) - 1;
        LivingEntity e = attackRecord.attacker();
        if (ThreadLocalRandom.current().nextFloat() < CHANCE[index] && e.getHealth() < e.getMaxHealth() * 0.5f) {
            e.heal(attackRecord.damage() * TRANSFER_RATE[index]);
        }
    }
}
