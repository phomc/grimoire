package dev.phomc.grimoire.enchantment;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.phomc.grimoire.Grimoire;
import dev.phomc.grimoire.enchantment.armor.AftershockEnchantment;
import dev.phomc.grimoire.enchantment.armor.AntidoteEnchantment;
import dev.phomc.grimoire.enchantment.armor.RefillEnchantment;
import dev.phomc.grimoire.enchantment.effect.active.*;
import dev.phomc.grimoire.enchantment.effect.passive.DecayEnchantment;
import dev.phomc.grimoire.enchantment.effect.passive.PetrifiedEnchantment;
import dev.phomc.grimoire.enchantment.effect.passive.VenomEnchantment;
import dev.phomc.grimoire.enchantment.melee.ColorShuffleEnchantment;
import dev.phomc.grimoire.enchantment.melee.VampireEnchantment;
import dev.phomc.grimoire.enchantment.tool.DiggerEnchantment;
import dev.phomc.grimoire.enchantment.tool.SmeltingEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EnchantmentRegistry {
    public static Map<ResourceLocation, GrimoireEnchantment> ALL = new LinkedHashMap<>(); // preserve order

    public static CompatibilityGraph COMPATIBILITY_GRAPH;
    public static SmeltingEnchantment SMELTING;
    public static DiggerEnchantment DIGGER;

    public static void init() {
        // melee
        registerEnchant(ColorShuffleEnchantment.class);
        registerEnchant(VampireEnchantment.class);

        // armor
        registerEnchant(AntidoteEnchantment.class);
        registerEnchant(RefillEnchantment.class); // helmet
        registerEnchant(AftershockEnchantment.class); // boots

        // tool
        registerEnchant(SmeltingEnchantment.class); // digger
        registerEnchant(DiggerEnchantment.class); // pickaxe

        // armor - effect (active)
        registerEnchant(WitherEnchantment.class);
        registerEnchant(PoisonEnchantment.class);
        registerEnchant(NauseaEnchantment.class);
        registerEnchant(FrozenEnchantment.class);
        registerEnchant(BlindnessEnchantment.class);
        registerEnchant(LevitationEnchantment.class);

        // armor - effect (passive)
        registerEnchant(DecayEnchantment.class);
        registerEnchant(PetrifiedEnchantment.class);
        registerEnchant(VenomEnchantment.class);

        COMPATIBILITY_GRAPH = new CompatibilityGraph();
        reportRegistration();
    }

    private static GrimoireEnchantment registerEnchant(Class<? extends GrimoireEnchantment> clazz) {
        try {
            String id = clazz.getSimpleName();
            if (!id.matches("\\w{3,}Enchantment")) throw new RuntimeException("Invalid Enchantment class name");
            id = id.substring(0, id.length() - "Enchantment".length());
            ResourceLocation identifier = new ResourceLocation("grimoire", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, id));
            GrimoireEnchantment instance = clazz.getDeclaredConstructor(ResourceLocation.class).newInstance(identifier);

            if (instance.getMaxLevel() > Byte.MAX_VALUE) {
                Grimoire.LOGGER.warn("Enchantment '{}' has excessive maximum level", id);
            }

            try {
                EnchantmentRegistry.class.getDeclaredField(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, id)).set(null, instance);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                //Grimoire.LOGGER.warn("No public field specified for enchantment '{}' ", id);
            }

            ALL.put(identifier, instance);
            Registry.register(BuiltInRegistries.ENCHANTMENT, identifier, instance);
            return instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reportRegistration() {
        Multimap<Enchantment.Rarity, ResourceLocation> map = ArrayListMultimap.create();
        for (Map.Entry<ResourceLocation, GrimoireEnchantment> entry : ALL.entrySet()) {
            map.put(entry.getValue().getRarity(), entry.getKey());
        }
        for (Enchantment.Rarity entry : map.keySet()) {
            Collection<ResourceLocation> collection = map.get(entry);
            Grimoire.LOGGER.info("{} {} enchantments: {}", collection.size(), entry, collection.stream()
                    .map(ResourceLocation::toString).collect(Collectors.joining(", ")));
        }
    }
}
