package dev.phomc.grimoire.enchantment.armor;

import dev.phomc.grimoire.enchantment.EnchantmentRarity;
import dev.phomc.grimoire.enchantment.EnchantmentTarget;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RefillEnchantment extends GrimoireEnchantment {
    public RefillEnchantment(@NotNull ResourceLocation identifier) {
        super(identifier, EnchantmentRarity.UNCOMMON, EnchantmentTarget.HELMET);
    }

    @Override
    public byte getMaxLevel() {
        return 2;
    }

    @Override
    public void onArmorTick(Player player, EquipmentSlot slot, ItemStack itemStack, byte level, int tick) {
        if (tick % 20 != 0) return;
        if (player.getHealth() < player.getMaxHealth() * level * 0.2f) {
            player.getInventory().items.stream()
                    .filter(i -> i.getItem().getFoodProperties() != null && i.getItem().getFoodProperties().getEffects().isEmpty())
                    .findAny()
                    .ifPresent(food -> player.eat(player.level, food));
        }
    }
}
