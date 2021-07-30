package com.celeste.library.spigot.view.event;

import com.celeste.library.spigot.exception.InvalidEventException;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

@Getter(AccessLevel.PACKAGE)
public final class EventWaiter<T extends Event> {

  private final Class<T> event;
  private final boolean cancelled;

  private Predicate<T> filter;
  private Consumer<T> action;

  @Setter
  private int executions;
  private boolean expires;

  private EventPriority priority;

  @SneakyThrows
  @SuppressWarnings({"UnstableApiUsage", "unchecked"})
  public EventWaiter() {
    try {
      final TypeToken<T> token = new TypeToken<T>(getClass()) {};
      final Type type = token.getType();

      this.event = (Class<T>) Class.forName(type.getTypeName());
      this.cancelled = true;

      this.filter = Objects::nonNull;

      this.executions = 0;
      this.expires = false;

      this.priority = EventPriority.NORMAL;
    } catch (Exception exception) {
      throw new InvalidEventException("The event used in EventWaiter is invalid. Exception: " + exception.getMessage());
    }
  }

  /**
   * Filters the contents of the Event.
   *
   * @param predicate Predicate of event
   * @return EventWaiter
   */
  public EventWaiter<T> filter(final Predicate<T> predicate) {
    filter = filter.and(predicate);
    return this;
  }

  /**
   * Expires after that number of executions
   *
   * @param executions Integer
   * @return EventWaiter
   */
  public EventWaiter<T> expireAfter(final int executions) {
    this.executions = executions;
    this.expires = true;
    return this;
  }

  /**
   * Sets the EventPriority of the EventWaiter
   *
   * @param priority EventPriority
   * @return EventWaiter
   */
  public EventWaiter<T> priority(final EventPriority priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Handler for the EventWaiter
   *
   * @param consumer Consumer of the event
   * @return EventWaiter
   */
  public EventWaiter<T> handler(final Consumer<T> consumer) {
    if (action != null) {
      this.action = action.andThen(consumer);
      return this;
    }

    this.action = consumer;
    return this;
  }

  /**
   * Registers EventWaiter as a listener.
   *
   * @param plugin Plugin
   */
  public void build(final Plugin plugin) {
    final EventListener<T> executor = new EventListener<>(this);
    Bukkit.getPluginManager().registerEvent(event, executor, priority, executor, plugin, cancelled);
  }

  /**
   * Cancels the event.
   *
   * @return EventWaiter
   */
  public EventWaiter<T> cancel() {
    return handler(event -> {
      if (event instanceof Cancellable) {
        ((Cancellable) event).setCancelled(true);
      }
    });
  }

}
