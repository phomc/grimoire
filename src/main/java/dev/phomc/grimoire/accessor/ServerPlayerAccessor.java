package dev.phomc.grimoire.accessor;

public interface ServerPlayerAccessor {
    boolean shouldIgnoreDigger();

    void ignoreDigger(boolean value);
}
