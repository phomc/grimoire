package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.event.AttackRecord;
import dev.phomc.grimoire.utils.DevModeUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.enchantment.Enchantment;

public class ProactiveEffectEnchantment extends EffectEnchantment {
    public ProactiveEffectEnchantment(ResourceLocation identifier, MobEffect effect, EffectStage... effectStages) {
        super(identifier, Enchantment.Rarity.COMMON, EnchantmentTarget.MELEE.or(EnchantmentTarget.RANGED), effect, effectStages);
        DevModeUtils.onEffectEnchantInit(identifier, "proactive", effect, effectStages);
    }

    @Override
    public void onAttack(AttackRecord attackRecord, int enchantLevel) {
        execute(attackRecord.attacker(), enchantLevel);
    }
}
