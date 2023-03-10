package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.attack.AbstractAttackEnchantment;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.ThreadLocalRandom;

public class ActiveEffectEnchantment extends AbstractAttackEnchantment {
    private final MobEffect effect;
    private final int[] amplifiers;
    private final float[] chances;
    private final int[] duration;

    public ActiveEffectEnchantment(ResourceLocation identifier, MobEffect effect, int[] amplifiers, int[] duration, float[] chances) {
        super(identifier, Enchantment.Rarity.COMMON);
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
    public int getMaxLevel() {
        return amplifiers.length;
    }

    @Override
    protected void execute(AttackRecord attackRecord, int level) {
        int index = Math.min(level, chances.length) - 1;
        float rand = ThreadLocalRandom.current().nextFloat();
        if (rand > chances[index]) return;
        attackRecord.victim().addEffect(new MobEffectInstance(effect, duration[index], amplifiers[index]));
    }
}
