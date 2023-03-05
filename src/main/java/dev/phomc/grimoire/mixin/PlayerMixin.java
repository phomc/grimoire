package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.event.AttackEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(
            method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;setAbsorptionAmount(F)V"
            )
    )
    protected void actuallyHurt(DamageSource damageSource, float f, CallbackInfo ci) {
        AttackEvent.handleHurt(damageSource, f, (LivingEntity) (Object) this);
    }
}
