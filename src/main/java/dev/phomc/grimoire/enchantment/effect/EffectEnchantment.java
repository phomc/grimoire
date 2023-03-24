package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class EffectEnchantment extends GrimoireEnchantment {
    private final MobEffect effect;
    private final EffectStage[] effectStages;

    public EffectEnchantment(ResourceLocation identifier, Rarity rarity, Predicate<Item> target, MobEffect effect, EffectStage... effectStages) {
        super(identifier, rarity, target);
        this.effect = effect;
        this.effectStages = effectStages;
    }

    @Override
    public int getMaxLevel() {
        return effectStages.length;
    }

    public void execute(LivingEntity entity, int level) {
        int index = clampLevel(level) - 1;
        float rand = ThreadLocalRandom.current().nextFloat();
        if (rand > effectStages[index].chance()) return;
        entity.addEffect(new MobEffectInstance(effect, effectStages[index].duration(), effectStages[index].amplifier()));
    }
}
