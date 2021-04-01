package com.celeste.library.spigot.util.item;

import org.bukkit.Location;
import org.bukkit.entity.Giant;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ItemGiantCreator {

  private ItemGiantCreator() {}

  /**
   * Creates a Giant item
   * @param location Location
   * @param item ItemStack
   */
  public static void create(@NotNull final Location location, @NotNull final ItemStack item) {
    final Giant giant = location.getWorld().spawn(location, Giant.class);
    final EntityEquipment equipment = Objects.requireNonNull(giant.getEquipment());

    equipment.setItemInHand(item);
    equipment.setItemInHandDropChance(0);

    giant.setCustomNameVisible(false);
    giant.setCanPickupItems(false);
    giant.setRemoveWhenFarAway(false);

    // remove AI from mob
  }

}
