package dev.phomc.grimoire.enchantment.property;

import java.util.concurrent.ThreadLocalRandom;

public interface NumericProperty extends Property<Double> {
    default boolean randomize(int level) {
        return ThreadLocalRandom.current().nextDouble() < evaluate(level);
    }
}
