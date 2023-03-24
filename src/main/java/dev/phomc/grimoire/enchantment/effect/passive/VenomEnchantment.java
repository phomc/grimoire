package dev.phomc.grimoire.enchantment.effect.passive;

import dev.phomc.grimoire.enchantment.effect.PassiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class VenomEnchantment extends PassiveEffectEnchantment {
    public VenomEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.POISON,
                new int[]{1, 2, 3, 4},
                new int[]{60, 80, 100, 100},
                new double[]{0.3, 0.4, 0.4, 0.5}
        );
    }
}
