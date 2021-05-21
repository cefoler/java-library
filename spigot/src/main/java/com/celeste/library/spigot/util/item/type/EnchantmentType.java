package com.celeste.library.spigot.util.item.type;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;

@Getter
@AllArgsConstructor
public enum EnchantmentType {

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
  PROTECTION_PROJECTILE("PROTECTION_PROJECTILE", "PROJECTILEPROTECTION", "PROJECTILE_PROTECTION",
      "PROJECTILE"),
  QUICK_CHARGE("QUICK_CHARGE", "QUICKCHARGE", "QUICK", "CHARGE", "FAST_CHARGE", "FASTCHARGE"),
  RIPTIDE("RIPTIDE", "RIP", "TIDE"),
  SILK_TOUCH("SILK_TOUCH", "SILKTOUCH", "ST"),
  SOUL_SPEED("SOUL_SPEED", "SOULSPEED", "SAND_SPEED", "SANDSPEED"),
  SWEEPING_EDGE("SWEEPING_EDGE", "SWEEPINGEDGE", "SWEEP", "SWEEPING", "EDGE"),
  THORNS("THORNS", "THORN"),
  VANISHING_CURSE("VANISHING_CURSE", "VANISHINGCURSE", "VANISH", "VANISHING"),
  WATER_WORKER("WATER_WORKER", "AQUAAFFINITY");

  private final List<String> names;

  EnchantmentType(final String... names) {
    this.names = ImmutableList.copyOf(names);
  }

  public static EnchantmentType getEnchantment(final String enchantment) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(enchantment.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid enchantment: " + enchantment));
  }

  public static Enchantment getRealEnchantment(final String enchantment) {
    final EnchantmentType enchantmentType = Arrays.stream(values())
        .filter(type -> type.getNames().contains(enchantment.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid enchantment: " + enchantment));

    return Enchantment.getByName(enchantmentType.name());
  }

  public static Enchantment getRealEnchantment(final EnchantmentType enchantment) {
    return Enchantment.getByName(enchantment.name());
  }

}
