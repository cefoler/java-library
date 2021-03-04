package com.celeste.util.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.ImmutableList.copyOf;
import static org.bukkit.enchantments.Enchantment.getByName;

@Getter
@AllArgsConstructor
public enum Enchantments {

    ARROW_DAMAGE("ARROW_DAMAGE", "POWER"),
    ARROW_FIRE("ARROW_FIRE", "FLAME"),
    ARROW_INFINITE("ARROW_INFINITE", "INFINITY", "INF"),
    ARROW_KNOCKBACK("ARROW_KNOCKBACK", "PUNCH"),
    BINDING_CURSE("BINDING_CURSE", "BIND", "BINDING"),
    CHANNELING("CHANNELING", "CHANNELLING", "CHANNEL"),
    DAMAGE_ALL("DAMAGE_ALL", "SHARPNESS", "SHARP"),
    DAMAGE_ARTHROPODS("DAMAGE_ARTHROPODS", "BANEOFARTHROPODS", "BANEOFARTHROPOD"),
    DAMAGE_UNDEAD("DAMAGE_UNDEAD", "SMITE"),
    DEPTH_STRIDER("DEPTH_STRIDER", "DEPTHSTRIDER", "DEPTH", "STRIDER"),
    DIG_SPEED("DIG_SPEED", "EFFICIENCY", "EFF"),
    DURABILITY("DURABILITY", "UNBREAKING", "UNB"),
    FIRE_ASPECT("FIRE_ASPECT", "FIREASPECT", "FIRE"),
    FROST_WALKER("FROST_WALKER", "FROSTWALKER", "FROST", "WALKER"),
    IMPALING("IMPALING", "IMPALE"),
    KNOCKBACK("KNOCKBACK", "KB"),
    LOOT_BONUS_BLOCKS("LOOT_BONUS_BLOCKS", "FORTUNE", "FOR"),
    LOOT_BONUS_MOBS("LOOT_BONUS_MOBS", "LOOTING", "LOOT"),
    LOYALTY("LOYALTY", "LOYAL"),
    LUCK("LUCK", "LUCKOFSEA", "LUCK_OF_SEA"),
    LURE("LURE", "RODLURE"),
    MENDING("MENDING", "REGEN"),
    MULTISHOT("MULTISHOT", "MULTI_SHOT", "MULTI", "SHOT", "TRIPLESHOT", "TRIPLE_SHOT", "TRIPLE"),
    OXYGEN("OXYGEN", "RESPIRATION", "BREATHING"),
    PIERCING("PIERCING", "PIER"),
    PROTECTION_ENVIRONMENTAL("PROTECTION_ENVIRONMENTAL", "PROTECTION", "PROTECT", "PROT", "PRO", "P"),
    PROTECTION_EXPLOSIONS("PROTECTION_EXPLOSIONS", "BLASTPROTECTION", "BLAST_PROTECTION", "BLAST"),
    PROTECTION_FALL("PROTECTION_FALL", "FEATHERFALLING", "FEATHER_FALLING", "FEATHER", "FALLING"),
    PROTECTION_FIRE("PROTECTION_FIRE", "FIREPROTECTION", "FIRE_PROTECTION"),
    PROTECTION_PROJECTILE("PROTECTION_PROJECTILE", "PROJECTILEPROTECTION", "PROJECTILE_PROTECTION", "PROJECTILE"),
    QUICK_CHARGE("QUICK_CHARGE", "QUICKCHARGE", "QUICK", "CHARGE", "FAST_CHARGE", "FASTCHARGE"),
    RIPTIDE("RIPTIDE", "RIP", "TIDE"),
    SILK_TOUCH("SILK_TOUCH", "SILKTOUCH", "ST"),
    SOUL_SPEED("SOUL_SPEED", "SOULSPEED", "SAND_SPEED", "SANDSPEED"),
    SWEEPING_EDGE("SWEEPING_EDGE", "SWEEPINGEDGE", "SWEEP", "SWEEPING", "EDGE"),
    THORNS("THORNS", "THORN"),
    VANISHING_CURSE("VANISHING_CURSE", "VANISHINGCURSE", "VANISH", "VANISHING"),
    WATER_WORKER("WATER_WORKER", "AQUAAFFINITY");

    private final List<String> names;

    Enchantments(final String... names) {
        this.names = copyOf(names);
    }

    /**
     * Gets type by name
     *
     * @param enchant String name
     * @return Enchantments
     */
    public static Enchantments getType(final String enchant) {
        return Arrays.stream(values())
            .filter(type -> type.getNames().contains(enchant.toUpperCase()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets Enchantment by name
     *
     * @param enchant String name
     * @return Enchantment
     */
    public static Enchantment getEnchant(final String enchant) {
        final Enchantments type = getType(enchant);

        if (type == null) {
            return null;
        }

        return getByName(type.name());
    }

}
