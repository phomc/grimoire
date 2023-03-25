package dev.phomc.grimoire.enchantment.ranged;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.ConditionalProperty;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.InfoProperty;
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

public class ExplosiveEnchantment extends GrimoireEnchantment {
    private static final double[] CHANCES = new double[]{0.5, 0.6, 0.7};
    private static final double[] RADIUS = new double[]{1.0, 1.5, 2.0};

    public ExplosiveEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.RARE, EnchantmentTarget.RANGED);

        createProperty("chance", (DecimalProperty) level -> CHANCES[level - getMinLevel()]);
        createProperty("radius", (DecimalProperty) level -> RADIUS[level - getMinLevel()]);
        createProperty("fire", new ConditionalProperty() {
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
    public void onProjectileHit(ProjectileHitRecord projectileHitRecord, int enchantLevel, MutableBoolean cancelled) {
        enchantLevel = clampLevel(enchantLevel);
        if (projectileHitRecord.shooter() instanceof ServerPlayer && requireDecimalProperty("chance").randomize(enchantLevel)) {
            Objects.requireNonNull(projectileHitRecord.weapon()).hurtAndBreak(3, projectileHitRecord.shooter(), p -> p.broadcastBreakEvent(p.getUsedItemHand()));
            Vec3 loc = projectileHitRecord.hitResult().getLocation();
            ServerLevel world = (ServerLevel) projectileHitRecord.projectile().level;
            DamageSource damageSource = world.damageSources().explosion(
                    projectileHitRecord.projectile(),
                    projectileHitRecord.shooter()
            );
            boolean fire = requireConditionalProperty("fire").evaluate(enchantLevel);
            float radius = requireDecimalProperty("radius").evaluate(enchantLevel).floatValue();
            Explosion explosion = new Explosion(world, null, damageSource, null, loc.x, loc.y, loc.z, radius, fire, Explosion.BlockInteraction.KEEP);
            explosion.explode();
            explosion.clearToBlow();
            for (ServerPlayer serverPlayer : world.players()) {
                if (!(serverPlayer.distanceToSqr(loc.x, loc.y, loc.z) < 4096.0)) continue;
                serverPlayer.connection.send(
                        new ClientboundExplodePacket(
                                loc.x, loc.y, loc.z, radius,
                                explosion.getToBlow(), explosion.getHitPlayers().get(serverPlayer)
                        )
                );
            }
            explosion.finalizeExplosion(true);
        }
    }
}
