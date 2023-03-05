package dev.phomc.grimoire.enchantment.effect.active;

import dev.phomc.grimoire.enchantment.effect.ActiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class LevitationEnchantment extends ActiveEffectEnchantment {
    public LevitationEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.LEVITATION,
                new int[]{0, 1, 1},
                new int[]{60, 100, 150},
                new float[]{0.1f, 0.2f, 0.2f}
        );
    }
}
