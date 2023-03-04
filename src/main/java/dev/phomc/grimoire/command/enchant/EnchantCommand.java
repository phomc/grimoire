package dev.phomc.grimoire.command.enchant;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.enchantment.GrimoireEnchantment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnchantCommand {
    public static final SuggestionProvider<CommandSourceStack> ALL_ENCHANTMENTS_SUGGESTION = SuggestionProviders.register(
        new ResourceLocation("grimoire", "all_enchantments"),
        (commandContext, suggestionsBuilder) -> {
            return SharedSuggestionProvider.suggestResource(EnchantmentRegistry.ALL.keySet(), suggestionsBuilder);
        }
    );

    public static final DynamicCommandExceptionType ERROR_UNKNOWN_ENCHANTMENT = new DynamicCommandExceptionType(id -> {
        return Component.translatable("grimoire.command.enchant.not_found", id);
    });
    public static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(who -> {
        return Component.translatable("grimoire.command.enchant.no_item", who);
    });
    public static final DynamicCommandExceptionType ERROR_WRONG_ITEM = new DynamicCommandExceptionType(who -> {
        return Component.translatable("grimoire.command.enchant.wrong_item", who);
    });

    public static GrimoireEnchantment getEnchantment(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        ResourceLocation resourceLocation = ResourceLocationArgument.getId(commandContext, string);
        GrimoireEnchantment enchantment = EnchantmentRegistry.ALL.get(resourceLocation);
        if (enchantment == null) {
            throw ERROR_UNKNOWN_ENCHANTMENT.create(resourceLocation);
        }
        return enchantment;
    }
}
