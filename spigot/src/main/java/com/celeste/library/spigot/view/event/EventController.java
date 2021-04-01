package com.celeste.library.spigot.view.event;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class EventController {

  /**
   * Checks if it's the same chunk
   * @param from Location
   * @param to Location
   *
   * @return boolean
   */
  public boolean isSameChunk(@NotNull final Location from, @NotNull final Location to) {
    if (!isSameWorld(from, to)) {
      return false;
    }

    final int fromX = from.getBlockX();
    final int fromZ = from.getBlockZ();

    final int toZ = to.getBlockZ();

    return (fromX >> 4) == (fromX >> 4) && (fromZ >> 4) == (toZ >> 4);
  }

  /**
   * Checks if it's the same block
   * @param from Location
   * @param to Location
   *
   * @return boolean
   */
  public boolean isSameBlock(@NotNull final Location from, @NotNull final Location to) {
    if (!isSameWorld(from, to)) {
      return false;
    }

    final int fromBlockX = from.getBlockX();
    final int fromBlockY = from.getBlockY();
    final int fromBlockZ = from.getBlockZ();

    final int toBlockX = to.getBlockX();
    final int toBlockY = to.getBlockY();
    final int toBlockZ = to.getBlockZ();

    return fromBlockX == toBlockX && fromBlockY == toBlockY && fromBlockZ == toBlockZ;
  }

  /**
   * Method to check if it is the same world.
   *
   * @param first  Location
   * @param target Location
   * @return boolean true if it's the same
   */
  public boolean isSameWorld(@NotNull final Location first, @NotNull final Location target) {
    return first.getWorld().getName().equalsIgnoreCase(target.getWorld().getName());
  }

}
