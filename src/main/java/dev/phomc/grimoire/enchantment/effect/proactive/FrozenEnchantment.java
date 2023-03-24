package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class FrozenEnchantment extends ProactiveEffectEnchantment {
    public FrozenEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.MOVEMENT_SLOWDOWN,
                new int[]{0, 1, 2, 3, 4},
                new int[]{40, 60, 80, 120, 120},
                new double[]{0.2, 0.2, 0.3, 0.3, 0.4}
        );
    }
}
