package dev.phomc.grimoire.enchantment.melee;

import dev.phomc.grimoire.accessor.ServerPlayerAccessor;
import dev.phomc.grimoire.accessor.VelocityNavigator;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.AttackRecord;
import dev.phomc.grimoire.utils.MathUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class DashEnchantment extends GrimoireEnchantment {
    private static final float[] CHANCES = new float[]{0.4f, 0.5f, 0.6f, 0.75f};
    private static final float[] DAMAGE_PERCENTAGE = new float[]{0.6f, 0.7f, 0.8f, 1.0f};
    private static final float SPECIAL_DAMAGE_PERCENTAGE = 1.2f;
    private static final float[] RADIUS = new float[]{0.5f, 0.6f, 0.7f, 0.7f};

    public DashEnchantment(ResourceLocation identifier) {
        super(identifier, Rarity.VERY_RARE, EnchantmentTarget.MELEE);
    }

    @Override
    public int getMaxLevel() {
        return CHANCES.length;
    }

    @Override
    public void onAttack(AttackRecord attackRecord, int level) {
        if (attackRecord.attacker() instanceof ServerPlayer player) {
            // prevent recursive calls; also intended feature: can not do dash while being navigated
            if (((ServerPlayerAccessor) player).isNavigated()) return;
            int index = clampLevel(level) - 1;

            if (ThreadLocalRandom.current().nextFloat() < CHANCES[index]) {
                Vec3 dir = MathUtils.getDirection(attackRecord.attacker());
                DamageSource damageSource = player.level.damageSources().playerAttack(player);
                VelocityNavigator nav = new VelocityNavigator(dir.multiply(8.0, 8.0, 8.0), 0.15f) {
                    @Override
                    public void onTick(boolean lastTick) {
                        Vec3 pos = player.position();
                        ((ServerLevel) player.level).sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
                        player.level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 2.0f, 1.0f);

                        float radius = RADIUS[index];
                        float damagePercent = lastTick && level == getMaxLevel() ? SPECIAL_DAMAGE_PERCENTAGE : DAMAGE_PERCENTAGE[index];
                        AABB box = player.getBoundingBox().inflate(radius, radius, radius);
                        List<LivingEntity> list = player.level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), player, box);
                        for (LivingEntity livingEntity : list) {
                            // multiple damages will be discarded because there is a short invulnerable time between
                            // attacks, so we have to reset it first:
                            livingEntity.invulnerableTime = 0;
                            livingEntity.hurt(damageSource, attackRecord.damage() * damagePercent);
                        }

                        Objects.requireNonNull(attackRecord.weapon()).hurtAndBreak(list.size(), player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
                        // stop if the item broke
                        if (attackRecord.weapon().isEmpty()) {
                            setCancelled(true);
                        }
                    }
                };
                ((ServerPlayerAccessor) player).navigate(nav);
            }
        }
    }
}
