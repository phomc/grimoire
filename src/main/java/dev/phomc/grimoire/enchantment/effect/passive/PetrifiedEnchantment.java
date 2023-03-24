package dev.phomc.grimoire.enchantment.effect.passive;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import dev.phomc.grimoire.enchantment.effect.PassiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class PetrifiedEnchantment extends PassiveEffectEnchantment {
    public PetrifiedEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.MOVEMENT_SLOWDOWN,
                new EffectStage(1, 60, 0.2),
                new EffectStage(2, 80, 0.3),
                new EffectStage(3, 120, 0.3),
                new EffectStage(4, 120, 0.4),
                new EffectStage(5, 150, 0.5)
        );
    }
}
