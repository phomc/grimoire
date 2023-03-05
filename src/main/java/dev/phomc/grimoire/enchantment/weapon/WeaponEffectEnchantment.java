package dev.phomc.grimoire.enchantment.weapon;

import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class WeaponEffectEnchantment extends GrimoireEnchantment {
    private final MobEffect effect;
    private final int[] amplifiers;
    private final float[] chances;
    private final int[] duration;

    public WeaponEffectEnchantment(ResourceLocation identifier, MobEffect effect, int[] amplifiers, int[] duration, float[] chances) {
        super(identifier, EnchantmentRarity.COMMON, EnchantmentTarget.WEAPON);
        this.effect = effect;
        this.amplifiers = amplifiers;
        if (amplifiers.length == 0) throw new IllegalArgumentException();
        this.duration = duration;
        if (amplifiers.length != duration.length) throw new IllegalArgumentException();
        this.chances = chances;
        if (amplifiers.length != chances.length) throw new IllegalArgumentException();
        Grimoire.LOGGER.debug("Enchantment {} amplifiers = {}, duration = {}, chances = {}", identifier, amplifiers, duration, chances);
    }

    @Override
    public byte getMaxLevel() {
        return (byte) amplifiers.length;
    }

    @Override
    public void doPostAttack(LivingEntity livingEntity, Entity entity, int level) {
        if (livingEntity instanceof Player && entity instanceof LivingEntity) {
            ItemStack item = livingEntity.getMainHandItem();
            if (item.isEmpty()) return;
            int lv = GrimoireItem.of(item).getEnchantmentFeature().getEnchantment(this);
            if (lv == 0) return;
            int index = Math.min(lv, chances.length) - 1;
            if (ThreadLocalRandom.current().nextFloat() > chances[index]) return;
            ((LivingEntity) entity).addEffect(new MobEffectInstance(
                    effect, duration[index], amplifiers[index]
            ));
        }
    }
}
