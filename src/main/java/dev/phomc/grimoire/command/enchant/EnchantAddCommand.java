package dev.phomc.grimoire.command.enchant;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.phomc.grimoire.command.SubCommand;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import dev.phomc.grimoire.item.GrimoireItem;
import dev.phomc.grimoire.item.features.EnchantmentFeature;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnchantAddCommand implements SubCommand {
    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.requires(forStaff)
            .then(
                Commands.argument("enchantment", ResourceLocationArgument.id())
                    .suggests(EnchantCommand.ALL_ENCHANTMENTS_SUGGESTION)
                    .executes(context -> enchant(context, 1, null))
                    .then(
                        Commands.argument("level", IntegerArgumentType.integer(1, Byte.MAX_VALUE))
                            .executes(context -> enchant(
                                    context,
                                    IntegerArgumentType.getInteger(context, "level"),
                                    null)
                            ).then(
                                Commands.argument("target", EntityArgument.player())
                                    .executes(context -> enchant(
                                            context,
                                            IntegerArgumentType.getInteger(context, "level"),
                                            EntityArgument.getPlayer(context, "target")
                                    ))
                            )
                    )
            );
    }

    public int enchant(CommandContext<CommandSourceStack> context, int lv, @Nullable Player target) throws CommandSyntaxException {
        GrimoireEnchantment enchantment = EnchantCommand.getEnchantment(context, "enchantment");
        if (enchantment.getMaxLevel() < lv) {
            throw EnchantCommand.ERROR_OVER_LEVEL.create(enchantment.getMaxLevel());
        }
        ServerPlayer executor = context.getSource().getPlayer();
        if (executor == null) throw new RuntimeException();
        if (target == null) target = executor;
        ItemStack itemStack = target.getMainHandItem();
        if (itemStack.isEmpty()) {
            throw EnchantCommand.ERROR_NO_ITEM.create(target.getName().getString());
        }
        if (!enchantment.getItemCheck().test(itemStack.getItem())) {
            throw EnchantCommand.ERROR_WRONG_ITEM.create(target.getName().getString());
        }
        if (!EnchantmentRegistry.COMPATIBILITY_GRAPH.isCompatible(itemStack, enchantment)) {
            throw EnchantCommand.ERROR_COMPATIBILITY.create();
        }
        GrimoireItem grimoireItem = GrimoireItem.of(itemStack);
        EnchantmentFeature enchantmentFeature = grimoireItem.getEnchantmentFeature();
        enchantmentFeature.setEnchantment(enchantment, lv);
        grimoireItem.pushChanges();
        target.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        executor.displayClientMessage(Component.translatable("grimoire.command.enchant.success", target.getName().getString()).withStyle(ChatFormatting.GREEN), false);
        return Command.SINGLE_SUCCESS;
    }
}
