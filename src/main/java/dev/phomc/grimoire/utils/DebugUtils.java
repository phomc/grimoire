package dev.phomc.grimoire.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.ArrayList;
import java.util.List;

public class DebugUtils {
    public static boolean isDebugEnabled() {
        return System.getenv("GRIMOIRE_DEBUG").equals("yes");
    }

    public static void debugEffect(ResourceLocation identifier, String type, MobEffect effect, int[] amplifiers, int[] duration, float[] chances) {
        if (!isDebugEnabled()) return;

        List<String> matrix = new ArrayList<>();

        for (int i = 0; i < amplifiers.length; i++) {
            matrix.add(String.valueOf(chances[i]));
            matrix.add(String.valueOf(amplifiers[i]));
            matrix.add(String.valueOf(duration[i]));
        }
        System.out.printf(
                "EnchantmentList.set(\"%s|%s\", { effect: \"%s\", params: [%s] })%n",
                type, identifier, effect.getDisplayName().getString(), String.join(",", matrix));
    }
}
