package dev.phomc.grimoire.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.phomc.grimoire.item.gemstone.Gemstone;

public class GemstoneArgument implements ArgumentType<Gemstone> {
    public static GemstoneArgument gemstone() {
        return new GemstoneArgument();
    }

    @Override
    public Gemstone parse(StringReader reader) {
        return Gemstone.valueOf(reader.readUnquotedString().toUpperCase());
    }
}
