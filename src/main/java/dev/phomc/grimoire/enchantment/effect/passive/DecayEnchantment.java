package dev.phomc.grimoire.enchantment.effect.passive;

import dev.phomc.grimoire.enchantment.effect.PassiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class DecayEnchantment extends PassiveEffectEnchantment {
    public DecayEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.WITHER,
                new int[]{1, 2, 3, 4},
                new int[]{80, 80, 100, 120},
                new double[]{0.3, 0.4, 0.5, 0.6}
        );
    }
}
