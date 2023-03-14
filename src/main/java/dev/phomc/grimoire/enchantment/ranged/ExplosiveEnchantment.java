package dev.phomc.grimoire.enchantment.ranged;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.ProjectileHitRecord;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ExplosiveEnchantment extends GrimoireEnchantment {
    private static final float[] CHANCES = new float[]{0.5f, 0.6f, 0.7f};
    private static final float[] RADIUS = new float[]{1.0f, 1.5f, 2.0f};

    public ExplosiveEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.RARE, EnchantmentTarget.RANGED);
    }

    @Override
    public int getMaxLevel() {
        return CHANCES.length;
    }

    @Override
    public void onProjectileHit(ProjectileHitRecord projectileHitRecord, int enchantLevel, MutableBoolean cancelled) {
        enchantLevel = clampLevel(enchantLevel);
        if (projectileHitRecord.shooter() instanceof ServerPlayer && ThreadLocalRandom.current().nextFloat() < CHANCES[enchantLevel - 1]) {
            Objects.requireNonNull(projectileHitRecord.weapon()).hurtAndBreak(3, projectileHitRecord.shooter(), livingEntity -> {
            });
            Vec3 loc = projectileHitRecord.hitResult().getLocation();
            ServerLevel world = (ServerLevel) projectileHitRecord.projectile().level;
            DamageSource damageSource = world.damageSources().explosion(
                    projectileHitRecord.projectile(),
                    projectileHitRecord.shooter()
            );
            boolean fire = enchantLevel == getMaxLevel();
            Explosion explosion = new Explosion(world, null, damageSource, null, loc.x, loc.y, loc.z,
                    RADIUS[enchantLevel - 1], fire, Explosion.BlockInteraction.KEEP);
            explosion.explode();
            explosion.clearToBlow();
            for (ServerPlayer serverPlayer : world.players()) {
                if (!(serverPlayer.distanceToSqr(loc.x, loc.y, loc.z) < 4096.0)) continue;
                serverPlayer.connection.send(
                        new ClientboundExplodePacket(
                                loc.x, loc.y, loc.z, RADIUS[enchantLevel - 1],
                                explosion.getToBlow(), explosion.getHitPlayers().get(serverPlayer)
                        )
                );
            }
            explosion.finalizeExplosion(true);
        }
    }
}
