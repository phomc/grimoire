package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.ActiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class WitherEnchantment extends ActiveEffectEnchantment {
    public WitherEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.WITHER,
                new int[]{0, 1, 2, 3, 4},
                new int[]{50, 50, 50, 80, 100},
                new float[]{0.1f, 0.2f, 0.25f, 0.3f, 0.35f}
        );
    }
}
