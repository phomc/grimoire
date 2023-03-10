package dev.phomc.grimoire.enchantment.melee;

import dev.phomc.grimoire.accessor.LightningBoltAccessor;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class ThunderEnchantment extends GrimoireEnchantment {

    public ThunderEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.UNCOMMON, EnchantmentTarget.MELEE);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public void onAttack(AttackRecord attackRecord, int enchantLevel) {
        if (attackRecord.attacker() instanceof ServerPlayer && ThreadLocalRandom.current().nextFloat() < enchantLevel * 0.1f) {
            Level level = attackRecord.victim().level;
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningBolt == null) return;
            lightningBolt.moveTo(attackRecord.victim().position());
            lightningBolt.setVisualOnly(ThreadLocalRandom.current().nextFloat() < 0.2f);
            // do not set this (since it triggers wrong criteria: CHANNELED_LIGHTNING)
            // lightningBolt.setCause((ServerPlayer) attackRecord.attacker());
            ((LightningBoltAccessor) lightningBolt).setOwner((ServerPlayer) attackRecord.attacker());
            level.addFreshEntity(lightningBolt);
        }
    }
}
