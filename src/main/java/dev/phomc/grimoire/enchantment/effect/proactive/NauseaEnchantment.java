package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class NauseaEnchantment extends ProactiveEffectEnchantment {
    public NauseaEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.CONFUSION,
                new int[]{0, 1, 1},
                new int[]{60, 80, 100},
                new float[]{0.2f, 0.3f, 0.4f}
        );
    }
}
