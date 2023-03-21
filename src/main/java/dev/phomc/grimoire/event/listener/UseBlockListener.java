package dev.phomc.grimoire.event.listener;

import dev.phomc.grimoire.item.ItemFeature;
import dev.phomc.grimoire.item.ItemHelper;
import dev.phomc.grimoire.item.ItemRegistry;
import dev.phomc.grimoire.item.custom.UnidentifiedGrimoireItem;
import dev.phomc.grimoire.item.features.GemstoneElementFeature;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class UseBlockListener implements UseBlockCallback {
    public UseBlockListener() {
        UseBlockCallback.EVENT.register(this);
    }

    @Override
    public InteractionResult interact(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        if (!player.isSpectator()) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.is(ItemRegistry.UNIDENTIFIED_GRIMOIRE) && world.getBlockState(hitResult.getBlockPos()).is(BlockTags.CAMPFIRES)) {
                ItemHelper.of(itemStack).requestFeature(ItemFeature.GEMSTONE_ELEMENT, (Consumer<GemstoneElementFeature>) feature -> {
                    if (feature.element == null) return;
                    ItemStack identified = UnidentifiedGrimoireItem.identify(feature.element);
                    player.setItemInHand(hand, identified);
                    player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3.0f, 1.0f);
                    CompletableFuture.runAsync(() -> {
                        for (double i = 0; i < 2f * Math.PI; i += Math.PI / 10.0) {
                            double x = 2 * Math.cos(i) + player.getX();
                            double z = 2 * Math.sin(i) + player.getZ();
                            ((ServerLevel) player.level).sendParticles(ParticleTypes.FLAME, x, player.getEyeY(), z, 10, 5, 5, 5, 1.0);
                        }
                    });
                });
            }
        }
        return InteractionResult.PASS;
    }
}
