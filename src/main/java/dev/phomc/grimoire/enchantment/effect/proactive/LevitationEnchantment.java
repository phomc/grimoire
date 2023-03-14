package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class LevitationEnchantment extends ProactiveEffectEnchantment {
    public LevitationEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.LEVITATION,
                new int[]{0, 1, 1},
                new int[]{60, 100, 150},
                new float[]{0.2f, 0.2f, 0.35f}
        );
    }
}
