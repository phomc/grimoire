package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At("TAIL"))
    protected void actuallyHurt(DamageSource damageSource, float f, CallbackInfo ci) {
        if (getType() == EntityType.SHEEP && damageSource instanceof EntityDamageSource) {
            Entity source = damageSource.getEntity();
            if (source instanceof Player) {
                ItemStack item = ((Player) source).getMainHandItem();
                if (item.isEmpty()) return;
                if (GrimoireItem.of(item).getEnchantmentFeature().getEnchantment(EnchantmentRegistry.COLOR_SHUFFLE) > 0) {
                    ((Sheep) (Object) this).setColor(DyeColor.values()[ThreadLocalRandom.current().nextInt(DyeColor.values().length)]);
                }
            }
        }
    }
}
