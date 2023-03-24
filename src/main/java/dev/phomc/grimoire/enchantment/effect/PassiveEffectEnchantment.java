package dev.phomc.grimoire.enchantment.effect;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.event.AttackRecord;
import dev.phomc.grimoire.utils.DevModeUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class PassiveEffectEnchantment extends EffectEnchantment {
    public PassiveEffectEnchantment(ResourceLocation identifier, MobEffect effect, EffectStage... effectStages) {
        super(identifier, Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR, effect, effectStages);
        DevModeUtils.onEffectEnchantInit(identifier, "passive", effect, effectStages);
    }

    @Override
    public void onAttacked(AttackRecord attackRecord, ItemStack armor, int level) {
        if (attackRecord.isRanged()) return; // must be damaged directly using melee weapon
        execute(attackRecord.attacker(), level);
    }
}
