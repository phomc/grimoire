package dev.phomc.grimoire.enchantment.armor;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.NaturalDamageRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AftershockEnchantment extends GrimoireEnchantment {
    private static final TargetingConditions TARGET_CONDITION = TargetingConditions.forCombat().range(5)
            .selector(livingEntity -> livingEntity.attackable() && livingEntity.isOnGround());

    public AftershockEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.UNCOMMON, EnchantmentTarget.BOOTS);

        createProperty("damage_rate", level -> {
            double power = level;
            if (level == getMaxLevel()) power++;
            return power / (double) getMaxLevel();
        });
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public void onNaturalDamaged(NaturalDamageRecord damageRecord, ItemStack armor, int level) {
        if (!damageRecord.isFall()) return;
        level = clampLevel(level);
        float damage = (float) (damageRecord.damage() * getPropertyValue("damage_rate", level));
        Objects.requireNonNull(armor).hurtAndBreak((int) damage >> 2, damageRecord.victim(), p -> p.broadcastBreakEvent(p.getUsedItemHand()));
        damageRecord.victim().getLevel().getNearbyEntities(
                LivingEntity.class, TARGET_CONDITION,
                damageRecord.victim(),
                damageRecord.victim().getBoundingBox().inflate(5, 5, 5)
        ).forEach(e -> e.hurt(damageRecord.damageSource(), damage));
    }
}
