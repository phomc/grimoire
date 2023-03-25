package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.IntegerProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class EffectEnchantment extends GrimoireEnchantment {
    private final MobEffect effect;
    private final EffectStage[] effectStages;

    public EffectEnchantment(ResourceLocation identifier, Rarity rarity, Predicate<Item> target, MobEffect effect, EffectStage... effectStages) {
        super(identifier, rarity, target);
        this.effect = effect;
        this.effectStages = effectStages;

        createProperty("amplifier", (IntegerProperty) level -> effectStages[level - getMinLevel()].amplifier());
        createProperty("duration", (IntegerProperty) level -> effectStages[level - getMinLevel()].duration());
        createProperty("chance", (DecimalProperty) level -> effectStages[level - getMinLevel()].chance());
    }

    @Override
    public int getMaxLevel() {
        return effectStages.length;
    }

    public void execute(LivingEntity entity, int level) {
        level = clampLevel(level);
        if (requireDecimalProperty("chance").randomize(level)) {
            entity.addEffect(new MobEffectInstance(
                    effect,
                    requireIntegerProperty("duration").evaluate(level),
                    requireIntegerProperty("amplifier").evaluate(level)
            ));
        }
    }
}
