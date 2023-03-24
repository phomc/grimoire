package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class LevitationEnchantment extends ProactiveEffectEnchantment {
    public LevitationEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.LEVITATION,
                new EffectStage(0, 60, 0.2),
                new EffectStage(1, 100, 0.2),
                new EffectStage(1, 150, 0.35)
        );
    }
}
