package dev.phomc.grimoire.enchantment.property;

public interface ConditionalProperty extends Property<Boolean> {
    @Override
    default Class<Boolean> type() {
        return Boolean.class;
    }
}
