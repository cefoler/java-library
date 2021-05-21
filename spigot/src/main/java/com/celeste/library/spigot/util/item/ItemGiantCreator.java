package com.celeste.library.spigot.util.item;

import com.celeste.library.spigot.util.ReflectionNms;
import java.util.Objects;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Giant;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public final class ItemGiantCreator {

  /**
   * Creates a Giant item
   *
   * @param location Location
   * @param item     ItemStack
   */
  @SneakyThrows
  public static void create(final Location location, final ItemStack item) {
    final Giant giant = location.getWorld().spawn(location, Giant.class);
    final EntityEquipment equipment = Objects.requireNonNull(giant.getEquipment());

    equipment.setItemInHand(item);
    equipment.setItemInHandDropChance(0);

    giant.setCustomNameVisible(false);
    giant.setCanPickupItems(false);
    giant.setRemoveWhenFarAway(false);
    // TODO: Remove AI from mob
  }

}
