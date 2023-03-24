package dev.phomc.grimoire.enchantment.property;

import java.util.concurrent.ThreadLocalRandom;

public interface IntegerProperty extends Property<Integer> {
    default boolean randomize(int level) {
        return ThreadLocalRandom.current().nextInt() < evaluate(level);
    }

    @Override
    default Class<Integer> type() {
        return Integer.class;
    }
}
