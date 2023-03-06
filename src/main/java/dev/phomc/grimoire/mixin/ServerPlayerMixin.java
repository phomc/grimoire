package dev.phomc.grimoire.mixin;

import com.mojang.authlib.GameProfile;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    private int armorTick;

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
            GrimoireItem.of(itemStack).getEnchantmentFeature().enchantments.forEach((key, value) -> {
                key.onArmorTick(this, slot, itemStack, value, ticks);
            });
        }
    }
}
