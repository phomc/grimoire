package dev.phomc.grimoire.accessor;

public interface ServerPlayerAccessor {
    boolean shouldIgnoreDiggingEnchantment();

    void ignoreDiggingEnchantment(boolean value);
}
