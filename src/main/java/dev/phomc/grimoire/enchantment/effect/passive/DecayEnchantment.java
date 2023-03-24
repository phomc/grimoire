package dev.phomc.grimoire.enchantment.effect.passive;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import dev.phomc.grimoire.enchantment.effect.PassiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class DecayEnchantment extends PassiveEffectEnchantment {
    public DecayEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.WITHER,
                new EffectStage(1, 80, 0.3),
                new EffectStage(2, 80, 0.4),
                new EffectStage(3, 100, 0.5),
                new EffectStage(4, 120, 0.6)
        );
    }
}
