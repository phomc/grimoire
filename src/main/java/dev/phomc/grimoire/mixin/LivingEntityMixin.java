package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.accessor.ProjectileMixinAccessor;
import dev.phomc.grimoire.event.AttackRecord;
import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At("TAIL"))
    protected void actuallyHurt(DamageSource damageSource, float f, CallbackInfo ci) {
        if (!(damageSource.getEntity() instanceof LivingEntity attacker)) return;
        LivingEntity victim = (LivingEntity) (Object) this;
        if (damageSource.typeHolder().is(DamageTypes.PLAYER_ATTACK) || damageSource.typeHolder().is(DamageTypes.MOB_ATTACK)) {
            ItemStack weapon = attacker.getMainHandItem();
            AttackRecord attackRecord = new AttackRecord(attacker, victim, null, f, weapon);
            GrimoireItem.of(weapon).getEnchantmentFeature().enchantments.forEach((key, value) -> {
                if (value < 1) return;
                key.onAttack(attackRecord, value);
            });
        }
        else if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && damageSource.getDirectEntity() instanceof Projectile projectile) {
            ItemStack weapon = ((ProjectileMixinAccessor) projectile).getWeapon();
            if (weapon == null) return;
            AttackRecord attackRecord = new AttackRecord(attacker, victim, projectile, f, weapon);
            GrimoireItem.of(weapon).getEnchantmentFeature().enchantments.forEach((key, value) -> {
                if (value < 1) return;
                key.onAttack(attackRecord, value);
            });
        }
    }
}
