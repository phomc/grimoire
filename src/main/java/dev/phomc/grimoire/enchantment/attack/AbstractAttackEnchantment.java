package dev.phomc.grimoire.enchantment.attack;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAttackEnchantment extends GrimoireEnchantment {
    public AbstractAttackEnchantment(@NotNull ResourceLocation identifier, @NotNull Rarity rarity) {
        super(identifier, rarity, EnchantmentTarget.MELEE.or(EnchantmentTarget.RANGED));
    }

    protected abstract void execute(AttackRecord attackRecord, int level);

    @Override
    public void onAttack(AttackRecord attackRecord, int level) {
        if (attackRecord.isRanged() || (attackRecord.weapon() != null && EnchantmentTarget.MELEE.test(attackRecord.weapon().getItem()))) {
            execute(attackRecord, level);
        }
    }
}
