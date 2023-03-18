package dev.phomc.grimoire.command;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Component;

public class CommandErrors {
    public static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(who -> {
        return Component.translatable("grimoire.command.no_item", who);
    });
    public static final DynamicCommandExceptionType ERROR_OVER_LEVEL = new DynamicCommandExceptionType(who -> {
        return Component.translatable("grimoire.command.enchant.level_too_high", who);
    });
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_ENCHANTMENT = new DynamicCommandExceptionType(id -> {
        return Component.translatable("grimoire.command.enchant.not_found", id);
    });
    public static final DynamicCommandExceptionType ERROR_WRONG_ITEM = new DynamicCommandExceptionType(who -> {
        return Component.translatable("grimoire.command.enchant.wrong_item", who);
    });
    public static final SimpleCommandExceptionType ERROR_COMPATIBILITY = new SimpleCommandExceptionType(
            Component.translatable("grimoire.command.enchant.incompatible")
    );
    public static final DynamicCommandExceptionType ERROR_INVALID_GEMSTONE = new DynamicCommandExceptionType(what -> {
        return Component.translatable("grimoire.command.give.invalid_gemstone", what);
    });
}
