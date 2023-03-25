package dev.phomc.grimoire.enchantment.property;

public class InfoProperty implements ConditionalProperty {
    @Override
    public boolean hasExtraDescription() {
        return true;
    }

    @Override
    public Boolean evaluate(int level) {
        return true;
    }
}
