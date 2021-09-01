package com.celeste.library.spigot.view.event;

import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventFilters {

  private static final EventController CONTROLLER;

  static {
    CONTROLLER = new EventController();
  }

  public static <T extends PlayerMoveEvent> Predicate<T> sameBlock() {
    return event -> CONTROLLER.isSameBlock(event.getFrom(), event.getTo());
  }

  public static <T extends PlayerMoveEvent> Predicate<T> ignoreIfSameBlock() {
    return (Predicate<T>) sameBlock().negate();
  }

  public static <T extends PlayerMoveEvent> Predicate<T> sameChunk() {
    return event -> CONTROLLER.isSameChunk(event.getFrom(), event.getTo());
  }

  public static <T extends PlayerMoveEvent> Predicate<T> ignoreIfSameChunk() {
    return (Predicate<T>) sameChunk().negate();
  }

  public static <T extends PlayerMoveEvent> Predicate<T> sameWorld() {
    return event -> CONTROLLER.isSameWorld(event.getFrom(), event.getTo());
  }

  public static <T extends PlayerMoveEvent> Predicate<T> ignoreIfSameWorld() {
    return (Predicate<T>) sameWorld().negate();
  }

  public static <T extends EntityEvent> Predicate<T> metadata(final String key) {
    return event -> event.getEntity().hasMetadata(key);
  }

  public static <T extends PlayerEvent> Predicate<T> permission(final String permission) {
    return event -> event.getPlayer().hasPermission(permission);
  }

  public static <T extends PlayerEvent> Predicate<T> equals(final Player player) {
    return event -> event.getPlayer().equals(player);
  }

}
