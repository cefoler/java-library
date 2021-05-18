package com.celeste.library.spigot.view.event;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public final class EventController {

  /**
   * Checks if it's the same chunk
   *
   * @param from Location
   * @param to Location
   * @return boolean
   */
  public boolean isSameChunk(final Location from, final Location to) {
    final Chunk chunkFrom = from.getChunk();
    final Chunk chunkTo = to.getChunk();

    return chunkFrom.equals(chunkTo);
  }

  /**
   * Checks if it's the same block
   *
   * @param from Location
   * @param to Location
   * @return boolean
   */
  public boolean isSameBlock(final Location from, final Location to) {
    if (!isSameWorld(from, to)) {
      return false;
    }

    final int blockXFrom = from.getBlockX();
    final int blockYFrom = from.getBlockY();
    final int blockZFrom = from.getBlockZ();

    final int blockXTo = to.getBlockX();
    final int blockYTo = to.getBlockY();
    final int blockZTo = to.getBlockZ();

    return blockXFrom == blockXTo && blockYFrom == blockYTo && blockZFrom == blockZTo;
  }

  /**
   * Method to check if it is the same world.
   *
   * @param from Location
   * @param to Location
   * @return boolean true if it's the same
   */
  public boolean isSameWorld(final Location from, final Location to) {
    final World worldFrom = from.getWorld();
    final World worldTo = to.getWorld();

    return worldFrom.getName().equalsIgnoreCase(worldTo.getName());
  }

}
