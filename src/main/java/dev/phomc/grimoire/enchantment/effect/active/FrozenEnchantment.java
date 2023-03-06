package dev.phomc.grimoire.enchantment.effect.active;

import dev.phomc.grimoire.enchantment.effect.ActiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class FrozenEnchantment extends ActiveEffectEnchantment {
    public FrozenEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.MOVEMENT_SLOWDOWN,
                new int[]{0, 1, 2, 3, 4, 5},
                new int[]{40, 60, 80, 120, 120, 150},
                new float[]{0.1f, 0.2f, 0.2f, 0.2f, 0.3f, 0.3f}
        );
    }
}