package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class ActiveEffectEnchantment extends GrimoireEnchantment {
    private final MobEffect effect;
    private final int[] amplifiers;
    private final float[] chances;
    private final int[] duration;

    public ActiveEffectEnchantment(ResourceLocation identifier, MobEffect effect, int[] amplifiers, int[] duration, float[] chances) {
        super(identifier, EnchantmentRarity.COMMON, EnchantmentTarget.MELEE.or(EnchantmentTarget.RANGED));
        this.effect = effect;
        this.amplifiers = amplifiers;
        if (amplifiers.length == 0) throw new IllegalArgumentException();
        this.duration = duration;
        if (amplifiers.length != duration.length) {
            throw new IllegalArgumentException(String.format("%s: len(amplifiers) != len(duration)", identifier));
        }
        this.chances = chances;
        if (amplifiers.length != chances.length) {
            throw new IllegalArgumentException(String.format("%s: len(amplifiers) != len(chances)", identifier));
        }
    }

    @Override
    public byte getMaxLevel() {
        return (byte) amplifiers.length;
    }

    public void execute(LivingEntity entity, byte level) {
        int index = Math.min(level, chances.length) - 1;
        float rand = ThreadLocalRandom.current().nextFloat();
        if (rand > chances[index]) return;
        entity.addEffect(new MobEffectInstance(effect, duration[index], amplifiers[index]));
    }

    @Override
    public void onAttack(AttackRecord attackRecord, byte level) {
        if (attackRecord.isRanged() || EnchantmentTarget.MELEE.test(attackRecord.weapon().getItem())) {
            execute(attackRecord.victim(), level);
        }
    }
}
