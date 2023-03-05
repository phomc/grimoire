package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.ActiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class BlindnessEnchantment extends ActiveEffectEnchantment {
    public BlindnessEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.BLINDNESS,
                new int[]{0, 0},
                new int[]{60, 60},
                new float[]{0.15f, 0.3f}
        );
    }
}
