package com.celeste.library.spigot.util.item.enchantments;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.error.ServerStartError;
import java.lang.reflect.Field;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public final class Glow extends Enchantment {

  static {
    try {
      final Class<?> clazz = Reflection.getClazz("org.bukkit.enchantments.Enchantment");
      final Field field = Reflection.getDcField(clazz, "acceptingNew");

      field.set(null, true);
    } catch (Exception exception) {
      throw new ServerStartError("Unable to register Glow enchantment.");
    }
  }

  public Glow(final int id) {
    super(id);
  }

  @Override
  public boolean canEnchantItem(final ItemStack item) {
    return false;
  }

  @Override
  public boolean conflictsWith(final Enchantment enchantment) {
    return false;
  }

  @Override
  public EnchantmentTarget getItemTarget() {
    return null;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public String getName() {
    return "Glow";
  }

  @Override
  public int getStartLevel() {
    return 0;
  }

}
