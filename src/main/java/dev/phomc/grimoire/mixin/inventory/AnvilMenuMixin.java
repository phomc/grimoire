package dev.phomc.grimoire.mixin.inventory;

import dev.phomc.grimoire.item.GrimoireItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(
            method = "createResult()V",
            at = @At("TAIL")
    )
    public void createResult(CallbackInfo ci) {
        ItemStack a = inputSlots.getItem(0);
        ItemStack b = inputSlots.getItem(1);
        if (a.is(Items.ENCHANTED_BOOK) || b.is(Items.ENCHANTED_BOOK)) {
            GrimoireItem gi = GrimoireItem.of(resultSlots.getItem(0));
            if (gi.getEnchantmentFeature().isEmpty()) return;
            gi.updateDisplay(); // set lore
            this.broadcastChanges();
        }
    }
}
