package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.event.EventDispatcher;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
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
}
