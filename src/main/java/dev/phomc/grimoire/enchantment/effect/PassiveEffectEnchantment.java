package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.IntegerProperty;
import dev.phomc.grimoire.event.AttackRecord;
import dev.phomc.grimoire.utils.DevModeUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.ThreadLocalRandom;

public class PassiveEffectEnchantment extends GrimoireEnchantment {
    private final MobEffect effect;
    private final int[] amplifiers;
    private final double[] chances;
    private final int[] duration;

    public PassiveEffectEnchantment(ResourceLocation identifier, MobEffect effect, int[] amplifiers, int[] duration, double[] chances) {
        super(identifier, Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR);
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
        DevModeUtils.onEffectEnchantInit(identifier, "passive", effect, amplifiers, duration, chances);

        createProperty("amplifier", (IntegerProperty) level -> amplifiers[level - getMinLevel()]);
        createProperty("duration", (IntegerProperty) level -> duration[level - getMinLevel()]);
        createProperty("chance", (DecimalProperty) level -> chances[level - getMinLevel()]);
    }

    @Override
    public int getMaxLevel() {
        return amplifiers.length;
    }

    public void execute(LivingEntity entity, int level) {
        int index = clampLevel(level) - 1;
        float rand = ThreadLocalRandom.current().nextFloat();
        if (rand > chances[index]) return;
        entity.addEffect(new MobEffectInstance(effect, duration[index], amplifiers[index]));
    }

    @Override
    public void onAttacked(AttackRecord attackRecord, ItemStack armor, int level) {
        if (attackRecord.isRanged()) return;
        execute(attackRecord.attacker(), level);
    }
}
