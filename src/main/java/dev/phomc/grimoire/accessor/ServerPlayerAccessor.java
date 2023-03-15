package dev.phomc.grimoire.accessor;

public interface ServerPlayerAccessor {
    boolean shouldIgnoreDiggingEnchantment();

    void ignoreDiggingEnchantment(boolean value);

    boolean navigate(VelocityNavigator navigator);

    boolean isNavigated();
}
