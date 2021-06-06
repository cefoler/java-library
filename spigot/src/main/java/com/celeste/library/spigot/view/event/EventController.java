package com.celeste.library.spigot.view.event;

import org.bukkit.Location;

public final class EventController {

  /**
   * Checks if it's the same chunk
   *
   * @param location Location
   * @param target Location
   * @return boolean
   */
  public boolean isSameChunk(final Location location, final Location target) {
    return location.getChunk().equals(target.getChunk());
  }

  /**
   * Checks if it's the same block
   *
   * @param location Location
   * @param target Location
   * @return boolean
   */
  public boolean isSameBlock(final Location location, final Location target) {
    return location.getBlock().equals(target.getBlock());
  }

  /**
   * Method target check if it is the same world.
   *
   * @param location Location
   * @param target Location
   * @return boolean true if it's the same
   */
  public boolean isSameWorld(final Location location, final Location target) {
    return location.getWorld().equals(target.getWorld());
  }

}
