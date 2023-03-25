package dev.phomc.grimoire.enchantment.melee;

import dev.phomc.grimoire.accessor.ServerPlayerAccessor;
import dev.phomc.grimoire.accessor.VelocityNavigator;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.ConditionalProperty;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.InfoProperty;
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

public class DashEnchantment extends GrimoireEnchantment {
    private static final double[] CHANCES = new double[]{0.4, 0.5, 0.6, 0.75};
    private static final double[] DAMAGE_PERCENTAGE = new double[]{0.6, 0.7, 0.8, 1.0};
    private static final double SPECIAL_DAMAGE_PERCENTAGE = 1.2f;
    private static final double[] RADIUS = new double[]{0.5, 0.6, 0.7, 0.7};

    public DashEnchantment(ResourceLocation identifier) {
        super(identifier, Rarity.VERY_RARE, EnchantmentTarget.MELEE);

        createProperty("chance", (DecimalProperty) level -> CHANCES[level - getMinLevel()]);
        createProperty("damageRatio", (DecimalProperty) level -> DAMAGE_PERCENTAGE[level - getMinLevel()]);
        createProperty("radius", (DecimalProperty) level -> RADIUS[level - getMinLevel()]);
        createProperty("specialDamageRatio", (DecimalProperty) level -> SPECIAL_DAMAGE_PERCENTAGE);
        createProperty("finalBonus", new ConditionalProperty() {
            @Override
            public boolean hasExtraDescription() {
                return true;
            }

            @Override
            public Boolean evaluate(int level) {
                return level == getMaxLevel();
            }
        });
        createProperty("cost", new InfoProperty());
    }

    @Override
    public int getMaxLevel() {
        return CHANCES.length;
    }

    @Override
    public void onAttack(AttackRecord attackRecord, int level) {
        level = clampLevel(level);

        if (attackRecord.attacker() instanceof ServerPlayer player) {
            // prevent recursive calls; also intended feature: can not do dash while being navigated
            if (((ServerPlayerAccessor) player).isNavigated()) return;

            if (requireDecimalProperty("chance").randomize(level)) {
                Vec3 dir = MathUtils.getDirection(attackRecord.attacker());
                DamageSource damageSource = player.level.damageSources().playerAttack(player);
                int finalLevel = level;
                VelocityNavigator nav = new VelocityNavigator(dir.multiply(8.0, 8.0, 8.0), 0.15f) {
                    @Override
                    public void onTick(boolean lastTick) {
                        Vec3 pos = player.position();
                        ((ServerLevel) player.level).sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
                        player.level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 2.0f, 1.0f);

                        double radius = requireDecimalProperty("radius").evaluate(finalLevel);
                        double damagePercent = lastTick && requireConditionalProperty("finalBonus").evaluate(finalLevel) ?
                                requireDecimalProperty("specialDamageRatio").evaluate(finalLevel) :
                                requireDecimalProperty("damageRatio").evaluate(finalLevel);
                        AABB box = player.getBoundingBox().inflate(radius, radius, radius);
                        List<LivingEntity> list = player.level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), player, box);
                        for (LivingEntity livingEntity : list) {
                            // multiple damages will be discarded because there is a short invulnerable time between
                            // attacks, so we have to reset it first:
                            livingEntity.invulnerableTime = 0;
                            livingEntity.hurt(damageSource, (float) (attackRecord.damage() * damagePercent));
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
