package com.celeste.library.spigot.util.injector;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public final class PlayerInjector {

  public static void reset(final Player player) {
    clear(player);
    heal(player);
    defaultSpeed(player);
  }

  public static void clear(final Player player) {
    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
    player.setLevel(0);
    player.setExp(0);

    for (PotionEffect effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }
  }

  public static void heal(final Player player) {
    player.setFoodLevel(20);
    player.setHealth(20);
  }

  public static void defaultSpeed(final Player player) {
    player.setWalkSpeed(0.2F);
    player.setFlySpeed(0.2F);
  }

}
