package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class PoisonEnchantment extends ProactiveEffectEnchantment {
    public PoisonEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.POISON,
                new EffectStage(0, 60, 0.2),
                new EffectStage(1, 60, 0.3),
                new EffectStage(2, 80, 0.4),
                new EffectStage(3, 100, 0.4),
                new EffectStage(4, 100, 0.5)
        );
    }
}
