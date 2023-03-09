package dev.phomc.grimoire.mixin;

import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(VillagerTrades.EnchantBookForEmeralds.class)
public class VillagerTradeEnchantedBook {
    @Inject(
            method = "getOffer(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/trading/MerchantOffer;",
            at = @At("RETURN"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void getOffer(Entity entity, RandomSource randomSource, CallbackInfoReturnable<MerchantOffer> cir,
                         List list, Enchantment enchantment, int i, ItemStack itemStack, int j){
        if (enchantment instanceof GrimoireEnchantment) {
            MerchantOffer merchantOffer = ((GrimoireEnchantment) enchantment).handleEnchantedBookOffer(entity, randomSource, cir.getReturnValue(), i);
            if (merchantOffer == null || !merchantOffer.getResult().is(Items.ENCHANTED_BOOK)) {
                throw new RuntimeException("invalid merchant offer caused by " + ((GrimoireEnchantment) enchantment).getIdentifier());
            }
            cir.setReturnValue(merchantOffer);
        }
    }
}
