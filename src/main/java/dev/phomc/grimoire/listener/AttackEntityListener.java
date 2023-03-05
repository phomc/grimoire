package dev.phomc.grimoire.listener;

import dev.phomc.grimoire.item.GrimoireItem;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class AttackEntityListener implements AttackEntityCallback {
    @Override
    public InteractionResult interact(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (player instanceof ServerPlayer && ((ServerPlayer) player).gameMode.isSurvival()) {
            if (!entity.isAttackable() || entity.skipAttackInteraction(player)) {
                return InteractionResult.PASS;
            }
            if (hand == InteractionHand.MAIN_HAND) {
                ItemStack item = player.getMainHandItem();
                if (!item.isEmpty()) {
                    GrimoireItem.of(item).getEnchantmentFeature().enchantments.forEach((key, value) -> key.onPlayerAttack(player, entity, value));
                }
            }
        }
        return InteractionResult.PASS;
    }
}
