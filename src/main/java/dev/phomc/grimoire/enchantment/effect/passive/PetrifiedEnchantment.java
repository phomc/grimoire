package dev.phomc.grimoire.enchantment.effect.passive;

import dev.phomc.grimoire.enchantment.effect.PassiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class PetrifiedEnchantment extends PassiveEffectEnchantment {
    public PetrifiedEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.MOVEMENT_SLOWDOWN,
                new int[]{0, 1, 1, 2},
                new int[]{60, 60, 80, 80},
                new float[]{0.1f, 0.15f, 0.15f, 0.2f}
        );
    }
}
