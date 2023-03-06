package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.accessor.ProjectileAccessor;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Projectile.class)
public class ProjectileMixin implements ProjectileAccessor {
    private ItemStack weapon;

    @Override
    public @Nullable ItemStack getWeapon() {
        return weapon;
    }

    @Override
    public void setWeapon(@Nullable ItemStack weapon) {
        this.weapon = weapon;
    }
}
