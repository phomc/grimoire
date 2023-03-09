package dev.phomc.grimoire.enchantment.armor;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.NaturalDamageRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class AftershockEnchantment extends GrimoireEnchantment {
    private static final TargetingConditions TARGET_CONDITION = TargetingConditions.forCombat().range(5)
            .selector(livingEntity -> livingEntity.attackable() && livingEntity.isOnGround());

    public AftershockEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.UNCOMMON, EnchantmentTarget.BOOTS);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    public float getAftershockDamage(float fallDamage, int lv) {
        float power = lv;
        if (lv == getMaxLevel()) {
            power += ThreadLocalRandom.current().nextFloat();
        }
        return fallDamage *  (power / getMaxLevel());
    }

    @Override
    public void onNaturalDamaged(NaturalDamageRecord damageRecord, ItemStack armor, int level) {
        if (!damageRecord.isFall()) return;
        damageRecord.victim().getLevel().getNearbyEntities(
                LivingEntity.class, TARGET_CONDITION,
                damageRecord.victim(),
                damageRecord.victim().getBoundingBox().inflate(5, 5, 5)
        ).forEach(e -> e.hurt(damageRecord.damageSource(), getAftershockDamage(damageRecord.damage(), level)));
    }
}
