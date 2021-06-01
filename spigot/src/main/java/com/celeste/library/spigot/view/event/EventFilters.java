package com.celeste.library.spigot.view.event;

import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@SuppressWarnings("unchecked")
public final class EventFilters {

  private static final EventController CONTROLLER;

  static {
    CONTROLLER = new EventController();
  }

  /**
   * Checks if it is the same block
   *
   * @param <T> Predicate
   * @return Predicate
   */
  public static <T extends PlayerMoveEvent> Predicate<T> sameBlock() {
    return event -> CONTROLLER.isSameBlock(event.getFrom(), event.getTo());
  }

  /**
   * Ignores if it's the same block
   *
   * @param <T> Predicate
   * @return Predicate
   */
  public static <T extends PlayerMoveEvent> Predicate<T> ignoreIfSameBlock() {
    return (Predicate<T>) sameBlock().negate();
  }

  /**
   * Checks if it is the same chunk
   *
   * @param <T> Predicate
   * @return Predicate
   */
  public static <T extends PlayerMoveEvent> Predicate<T> sameChunk() {
    return event -> CONTROLLER.isSameChunk(event.getFrom(), event.getTo());
  }

  /**
   * Ignores if it's the same chunk
   *
   * @param <T> Predicate
   * @return Predicate
   */
  public static <T extends PlayerMoveEvent> Predicate<T> ignoreIfSameChunk() {
    return (Predicate<T>) sameChunk().negate();
  }

  /**
   * Checks if it is the same world
   *
   * @param <T> Predicate
   * @return Predicate
   */
  public static <T extends PlayerMoveEvent> Predicate<T> sameWorld() {
    return event -> CONTROLLER.isSameWorld(event.getFrom(), event.getTo());
  }

  /**
   * Ignores if it's the same world
   *
   * @param <T> Predicate
   * @return Predicate
   */
  public static <T extends PlayerMoveEvent> Predicate<T> ignoreIfSameWorld() {
    return (Predicate<T>) sameWorld().negate();
  }

  /**
   * Checks if the entity has metadata.
   *
   * @param <T> Predicate
   * @param key Key for the metadata
   * @return Predicate
   */
  public static <T extends EntityEvent> Predicate<T> metadata(final String key) {
    return event -> event.getEntity().hasMetadata(key);
  }

  /**
   * Checks if player has permission
   *
   * @param permission String of the permission
   * @param <T>        Predicate
   * @return Predicate
   */
  public static <T extends PlayerEvent> Predicate<T> permission(final String permission) {
    return event -> event.getPlayer().hasPermission(permission);
  }

  /**
   * Checks if player equals to target
   *
   * @param player Player
   * @param <T> Predicate
   * @return Predicate
   */
  public static <T extends PlayerEvent> Predicate<T> equals(final Player player) {
    return event -> event.getPlayer().equals(player);
  }

}
