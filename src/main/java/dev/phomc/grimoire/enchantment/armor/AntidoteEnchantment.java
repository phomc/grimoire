package dev.phomc.grimoire.enchantment.armor;

import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AntidoteEnchantment extends GrimoireEnchantment {

    public AntidoteEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, EnchantmentRarity.RARE, EnchantmentTarget.ARMOR);
    }

    public byte getMaxLevel() {
        return 3;
    }

    @Override
    public void onArmorTick(Player player, EquipmentSlot slot, ItemStack itemStack, byte level, int tick) {
        if (tick % 20 == 0 && ThreadLocalRandom.current().nextFloat() < (level + 1) * 0.05f) {
            List<MobEffectInstance> harmfulEffects = player.getActiveEffects().stream()
                    .filter(mobEffectInstance -> {
                        MobEffectCategory category = mobEffectInstance.getEffect().getCategory();
                        return category == MobEffectCategory.HARMFUL ||
                                (category == MobEffectCategory.NEUTRAL && level > 1);
                    }).toList();
            if (harmfulEffects.isEmpty()) return;
            MobEffectInstance eff = harmfulEffects.get(ThreadLocalRandom.current().nextInt(harmfulEffects.size()));
            if (eff.amplifier == 0) {
                player.removeEffect(eff.getEffect());
            } else {
                eff.amplifier--;
                player.forceAddEffect(eff, null);
            }
        }
    }

}
