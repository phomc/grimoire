package dev.phomc.grimoire.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.phomc.grimoire.enchantment.EnchantmentRegistry;
import dev.phomc.grimoire.item.gemstone.Gemstone;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

public class Suggestions {
    public static final SuggestionProvider<CommandSourceStack> ALL_ENCHANTMENTS_SUGGESTION = SuggestionProviders.register(
            new ResourceLocation("grimoire", "all_enchantments"),
            (commandContext, suggestionsBuilder) -> {
                return SharedSuggestionProvider.suggestResource(EnchantmentRegistry.ALL.keySet(), suggestionsBuilder);
            }
    );

    public static final SuggestionProvider<CommandSourceStack> ALL_GEMSTONE_SUGGESTION = SuggestionProviders.register(
            new ResourceLocation("grimoire", "all_gemstones"),
            (commandContext, suggestionsBuilder) -> {
                return SharedSuggestionProvider.suggest(Arrays.stream(Gemstone.values()).map(Gemstone::getId).toList(), suggestionsBuilder);
            }
    );
}
