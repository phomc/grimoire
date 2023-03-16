package dev.phomc.grimoire.mixin.entity;

import dev.phomc.grimoire.accessor.LightningBoltAccessor;
import dev.phomc.grimoire.event.EventDispatcher;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V"
            )
    )
    protected void actuallyHurt(DamageSource damageSource, float f, CallbackInfo ci) {
        EventDispatcher.handleHurt(damageSource, f, (LivingEntity) (Object) this);
    }

    @Override
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
        ServerPlayer p = ((LightningBoltAccessor) lightningBolt).getOwner();
        if (this.is(p) || this.isAlliedTo(p)) return;
        super.thunderHit(serverLevel, lightningBolt);
    }
}
