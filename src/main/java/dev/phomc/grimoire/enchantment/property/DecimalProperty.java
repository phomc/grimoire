package dev.phomc.grimoire.enchantment.property;

import java.util.concurrent.ThreadLocalRandom;

public interface DecimalProperty extends Property<Double> {
    default boolean randomize(int level) {
        return ThreadLocalRandom.current().nextDouble() < evaluate(level);
    }

    @Override
    default Class<Double> type() {
        return Double.class;
    }
}
