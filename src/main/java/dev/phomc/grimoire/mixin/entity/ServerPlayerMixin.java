package dev.phomc.grimoire.mixin.entity;

import com.mojang.authlib.GameProfile;
import dev.phomc.grimoire.accessor.ServerPlayerAccessor;
import dev.phomc.grimoire.accessor.VelocityNavigator;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements ServerPlayerAccessor {
    private int armorTick;
    private boolean ignoreDiggingEnchantment;
    private VelocityNavigator velocityNavigator;

    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        int ticks = armorTick++;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) continue;
            ItemStack itemStack = getItemBySlot(slot);
            if (itemStack.isEmpty()) continue;
            GrimoireItem.of(itemStack).getEnchantmentFeature().iterateEnchantments(itemStack, (key, value) -> {
                key.onArmorTick(this, slot, itemStack, value, ticks);
            });
        }

        if (velocityNavigator != null) {
            if (velocityNavigator.isCancelled()) {
                velocityNavigator = null;
            } else {
                setDeltaMovement(velocityNavigator.getDeltaMovement());
                hurtMarked = true;
                // TODO handle collision -> end immediately
                if (velocityNavigator.nextTick()) {
                    velocityNavigator.onTick(false);
                } else {
                    velocityNavigator.onTick(true);
                    velocityNavigator = null;
                }
            }
        }
    }

    @Override
    public boolean shouldIgnoreDiggingEnchantment() {
        return ignoreDiggingEnchantment;
    }

    @Override
    public void ignoreDiggingEnchantment(boolean value) {
        ignoreDiggingEnchantment = value;
    }

    @Override
    public synchronized boolean navigate(VelocityNavigator navigator) {
        if (velocityNavigator != null) return false;
        velocityNavigator = navigator;
        return true;
    }

    @Override
    public boolean isNavigated() {
        return velocityNavigator != null;
    }
}
