package dev.phomc.grimoire.enchantment.melee.effect;

import dev.phomc.grimoire.enchantment.melee.MeleeEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class PoisonEnchantment extends MeleeEffectEnchantment {
    public PoisonEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.POISON,
                new int[]{0, 1, 2, 3, 4},
                new int[]{50, 50, 50, 80, 100},
                new float[]{0.1f, 0.2f, 0.25f, 0.3f, 0.35f}
        );
    }
}
