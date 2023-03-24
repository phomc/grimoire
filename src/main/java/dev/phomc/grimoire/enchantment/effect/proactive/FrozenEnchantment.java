package dev.phomc.grimoire.enchantment.effect.proactive;

import dev.phomc.grimoire.enchantment.effect.EffectStage;
import dev.phomc.grimoire.enchantment.effect.ProactiveEffectEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class FrozenEnchantment extends ProactiveEffectEnchantment {
    public FrozenEnchantment(ResourceLocation identifier) {
        super(identifier, MobEffects.MOVEMENT_SLOWDOWN,
                new EffectStage(0, 40, 0.2),
                new EffectStage(1, 60, 0.2),
                new EffectStage(2, 80, 0.3),
                new EffectStage(3, 120, 0.3),
                new EffectStage(4, 120, 0.4)
        );
    }
}
