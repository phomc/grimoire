package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class NauseaEnchantment extends ProactiveEffectEnchantment {
    public NauseaEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.CONFUSION,
                new EffectStage(0, 60, 0.2),
                new EffectStage(1, 80, 0.3),
                new EffectStage(1, 100, 0.4)
        );
    }
}
