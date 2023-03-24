package dev.phomc.grimoire.enchantment.attack;

import dev.phomc.grimoire.accessor.LightningBoltAccessor;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.event.AttackRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ThunderEnchantment extends AbstractAttackEnchantment {

    public ThunderEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Rarity.UNCOMMON);

        createProperty("chance", (DecimalProperty) level -> level * 0.3);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    protected void execute(AttackRecord attackRecord, int enchantLevel) {
        enchantLevel = clampLevel(enchantLevel);
        if (attackRecord.attacker() instanceof ServerPlayer && requireDecimalProperty("chance").randomize(enchantLevel)) {
            Level level = attackRecord.victim().level;
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningBolt == null) return;
            lightningBolt.moveTo(attackRecord.victim().position());
            lightningBolt.setVisualOnly(ThreadLocalRandom.current().nextFloat() < 0.2f);
            // do not set this (since it triggers wrong criteria: CHANNELED_LIGHTNING)
            // lightningBolt.setCause((ServerPlayer) attackRecord.attacker());
            ((LightningBoltAccessor) lightningBolt).setOwner((ServerPlayer) attackRecord.attacker());
            level.addFreshEntity(lightningBolt);
            Objects.requireNonNull(attackRecord.weapon()).hurtAndBreak(2, attackRecord.attacker(), p -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }
    }
}
