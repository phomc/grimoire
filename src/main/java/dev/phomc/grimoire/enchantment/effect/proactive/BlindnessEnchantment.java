package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class BlindnessEnchantment extends ProactiveEffectEnchantment {
    public BlindnessEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.BLINDNESS,
                new int[]{0, 0},
                new int[]{60, 100},
                new double[]{0.15, 0.3}
        );
    }
}
