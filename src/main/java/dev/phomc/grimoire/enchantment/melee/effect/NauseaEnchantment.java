package dev.phomc.grimoire.enchantment.melee.effect;

import dev.phomc.grimoire.enchantment.melee.MeleeEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class NauseaEnchantment extends MeleeEffectEnchantment {
    public NauseaEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.CONFUSION,
                new int[]{0, 1, 1},
                new int[]{30, 60, 100},
                new float[]{0.1f, 0.2f, 0.25f}
        );
    }
}
