package dev.phomc.grimoire.accessor;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ProjectileMixinAccessor {
    @Nullable ItemStack getWeapon();

    void setWeapon(@Nullable ItemStack weapon);
}
