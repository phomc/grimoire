package dev.phomc.grimoire.enchantment.melee;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.InfoProperty;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class VampireEnchantment extends GrimoireEnchantment {
    private static final double[] CHANCE = new double[]{0.3, 0.35, 0.4, 0.45, 0.5};
    private static final double[] TRANSFER_RATE = new double[]{0.4, 0.5, 0.6, 0.7, 0.8};

    public VampireEnchantment(ResourceLocation identifier) {
        super(identifier, Rarity.RARE, EnchantmentTarget.MELEE);

        createProperty("chance", (DecimalProperty) level -> CHANCE[level - getMinLevel()]);
        createProperty("transferRate", (DecimalProperty) level -> TRANSFER_RATE[level - getMinLevel()]);
        createProperty("cost", new InfoProperty());
    }

    @Override
    public int getMaxLevel() {
        return CHANCE.length;
    }

    @Override
    public void onAttack(AttackRecord attackRecord, int level) {
        level = clampLevel(level);
        LivingEntity e = attackRecord.attacker();
        if (requireDecimalProperty("chance").randomize(level) && e.getHealth() < e.getMaxHealth() * 0.5f) {
            Objects.requireNonNull(attackRecord.weapon()).hurtAndBreak(2, e, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
            e.heal((float) (attackRecord.damage() * requireDecimalProperty("transferRate").evaluate(level)));
        }
    }
}
