package dev.phomc.grimoire.enchantment.armor;

import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.enchantment.property.ConditionalProperty;
import dev.phomc.grimoire.enchantment.property.DecimalProperty;
import dev.phomc.grimoire.enchantment.property.InfoProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class AntidoteEnchantment extends GrimoireEnchantment {

    public AntidoteEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR);

        createProperty("chance", (DecimalProperty) level -> (level + 1) * 0.05);
        createProperty("extraEffect", new ConditionalProperty() {
            @Override
            public boolean hasExtraDescription() {
                return true;
            }

            @Override
            public Boolean evaluate(int level) {
                return level > 1;
            }
        });
        createProperty("cost", new InfoProperty());
    }

    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void onArmorTick(Player player, EquipmentSlot slot, ItemStack itemStack, int level, int tick) {
        level = clampLevel(level);
        if (tick % 20 == 0 && requireDecimalProperty("chance").randomize(level)) {
            boolean extraEffect = requireConditionalProperty("extraEffect").evaluate(level);
            List<MobEffectInstance> harmfulEffects = player.getActiveEffects().stream()
                    .filter(mobEffectInstance -> {
                        MobEffectCategory category = mobEffectInstance.getEffect().getCategory();
                        return category == MobEffectCategory.HARMFUL ||
                                (category == MobEffectCategory.NEUTRAL && extraEffect);
                    }).toList();
            if (harmfulEffects.isEmpty()) return;
            MobEffectInstance eff = harmfulEffects.get(ThreadLocalRandom.current().nextInt(harmfulEffects.size()));
            if (eff.amplifier == 0) {
                player.removeEffect(eff.getEffect());
            } else {
                eff.amplifier--;
                player.forceAddEffect(eff, null);
            }
            Objects.requireNonNull(itemStack).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }
    }

}
