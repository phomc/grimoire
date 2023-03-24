package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class WitherEnchantment extends ProactiveEffectEnchantment {
    public WitherEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.WITHER,
                new EffectStage(0, 60, 0.2),
                new EffectStage(1, 80, 0.3),
                new EffectStage(2, 80, 0.4),
                new EffectStage(3, 100, 0.5),
                new EffectStage(4, 100, 0.5)
        );
    }
}
