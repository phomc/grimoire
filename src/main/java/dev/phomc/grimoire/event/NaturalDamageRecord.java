package dev.phomc.grimoire.event;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record NaturalDamageRecord(LivingEntity victim, float damage, DamageSource damageSource) {
    public boolean isFall() {
        return damageSource.is(DamageTypeTags.IS_FALL);
    }
}
