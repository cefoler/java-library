package com.celeste.library.spigot.util.item.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public final class Glow extends Enchantment {

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
