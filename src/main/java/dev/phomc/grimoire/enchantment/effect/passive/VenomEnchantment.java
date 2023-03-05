package dev.phomc.grimoire.enchantment.effect.passive;

import dev.phomc.grimoire.enchantment.effect.PassiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class VenomEnchantment extends PassiveEffectEnchantment {
    public VenomEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.POISON,
                new int[]{0, 1, 1, 2},
                new int[]{60, 60, 80, 100},
                new float[]{0.15f, 0.2f, 0.2f, 0.2f}
        );
    }
}
