package com.celeste.library.spigot.view.event;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class EventFilters {

  private static final EventController CONTROLLER = new EventController();

  private EventFilters() {}

  /**
   * Checks if it is the same block
   *
   * @param <E> Predicate
   * @return Predicate
   */
  @NotNull
  public static <E extends PlayerMoveEvent> Predicate<E> sameBlock() {
    return e -> CONTROLLER.isSameBlock(e.getFrom(), e.getTo());
  }

  /**
   * Ignores if it's the same block
   *
   * @param <E> Predicate
   * @return Predicate
   */
  @NotNull @SuppressWarnings("unchecked")
  public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameBlock() {
    return (Predicate<E>) sameBlock().negate();
  }

  /**
   * Checks if it is the same chunk
   *
   * @param <E> Predicate
   * @return Predicate
   */
  @NotNull
  public static <E extends PlayerMoveEvent> Predicate<E> sameChunk() {
    return e -> CONTROLLER.isSameChunk(e.getFrom(), e.getTo());
  }

  /**
   * Ignores if it's the same chunk
   *
   * @param <E> Predicate
   * @return Predicate
   */
  @NotNull @SuppressWarnings("unchecked")
  public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameChunk() {
    return (Predicate<E>) sameChunk().negate();
  }

  /**
   * Checks if it is the same world
   *
   * @param <E> Predicate
   * @return Predicate
   */
  @NotNull
  public static <E extends PlayerMoveEvent> Predicate<E> sameWorld() {
    return e -> CONTROLLER.isSameWorld(e.getFrom(), e.getTo());
  }

  /**
   * Ignores if it's the same world
   *
   * @param <E> Predicate
   * @return Predicate
   */
  @NotNull @SuppressWarnings("unchecked")
  public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameWorld() {
    return (Predicate<E>) sameWorld().negate();
  }

  /**
   * Checks if the entity has metadata.
   *
   * @param <E>         Predicate
   * @param key Key for the metadata
   * @return Predicate
   */
  @NotNull
  public static <E extends EntityEvent> Predicate<E> metadata(@NotNull final String key) {
    return event -> event.getEntity().hasMetadata(key);
  }

  /**
   * Checks if player has permission
   *
   * @param permission String of the permission
   * @param <E>        Predicate
   * @return Predicate
   */
  @NotNull
  public static <E extends PlayerEvent> Predicate<E> permission(@NotNull final String permission) {
    return event -> event.getPlayer().hasPermission(permission);
  }

  /**
   * Checks if player equals to target
   *
   * @param player Player
   * @param <E>    Predicate
   * @return Predicate
   */
  @NotNull
  public static <E extends PlayerEvent> Predicate<E> equals(@NotNull final Player player) {
    return event -> event.getPlayer().getUniqueId().equals(player.getUniqueId());
  }

}
