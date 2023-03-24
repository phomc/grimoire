package dev.phomc.grimoire.enchantment.property;

public interface Property<T> {
    T evaluate(int level);
}
