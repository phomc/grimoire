package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class WitherEnchantment extends ProactiveEffectEnchantment {
    public WitherEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.WITHER,
                new int[]{0, 1, 2, 3, 4},
                new int[]{60, 80, 80, 100, 100},
                new float[]{0.2f, 0.3f, 0.4f, 0.5f, 0.5f}
        );
    }
}
