package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class BlindnessEnchantment extends ProactiveEffectEnchantment {
    public BlindnessEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.BLINDNESS,
                new EffectStage(0, 60, 0.15),
                new EffectStage(0, 100, 0.3)
        );
    }
}
