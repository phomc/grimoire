package dev.phomc.grimoire.enchantment.effect.passive;

import dev.phomc.grimoire.enchantment.effect.PassiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class PetrifiedEnchantment extends PassiveEffectEnchantment {
    public PetrifiedEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.MOVEMENT_SLOWDOWN,
                new int[]{1, 2, 3, 4, 5},
                new int[]{60, 80, 120, 120, 150},
                new float[]{0.2f, 0.3f, 0.3f, 0.4f, 0.5f}
        );
    }
}
