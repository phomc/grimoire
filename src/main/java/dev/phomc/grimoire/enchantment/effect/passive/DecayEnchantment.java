package dev.phomc.grimoire.enchantment.effect.passive;

import dev.phomc.grimoire.enchantment.effect.PassiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class DecayEnchantment extends PassiveEffectEnchantment {
    public DecayEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.WITHER,
                new int[]{0, 1, 1},
                new int[]{60, 80, 120},
                new float[]{0.1f, 0.1f, 0.2f}
        );
    }
}
