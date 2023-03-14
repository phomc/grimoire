package dev.phomc.grimoire.enchantment.melee;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class VampireEnchantment extends GrimoireEnchantment {
    private static final float[] CHANCE = new float[]{0.3f, 0.35f, 0.4f, 0.45f, 0.5f};
    private static final float[] TRANSFER_RATE = new float[]{0.4f, 0.5f, 0.6f, 0.7f, 0.8f};

    public VampireEnchantment(ResourceLocation identifier) {
        super(identifier, Rarity.RARE, EnchantmentTarget.MELEE);
    }

    @Override
    public int getMaxLevel() {
        return CHANCE.length;
    }

    @Override
    public void onAttack(AttackRecord attackRecord, int level) {
        int index = clampLevel(level) - 1;
        LivingEntity e = attackRecord.attacker();
        if (ThreadLocalRandom.current().nextFloat() < CHANCE[index] && e.getHealth() < e.getMaxHealth() * 0.5f) {
            Objects.requireNonNull(attackRecord.weapon()).hurtAndBreak(1, e, livingEntity -> {});
            e.heal(attackRecord.damage() * TRANSFER_RATE[index]);
        }
    }
}
