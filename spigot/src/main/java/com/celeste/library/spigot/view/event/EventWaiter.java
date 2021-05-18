package com.celeste.library.spigot.view.event;

import com.google.gson.reflect.TypeToken;
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
public final class EventWaiter<E extends Event> {

  private final Class<E> event;
  private final boolean ignoreCancelled;
  private Predicate<E> filter;
  private Consumer<E> action;
  @Setter
  private int executions;
  private boolean expireAfterExecute;
  private EventPriority priority;

  /**
   * Event waiter constructor
   */
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public EventWaiter() {
    final TypeToken<? extends EventWaiter> token = TypeToken.get(getClass());
    final Type type = token.getType();
    this.event = (Class<E>) Class.forName(type.getTypeName());

    this.filter = Objects::nonNull;
    this.expireAfterExecute = false;
    this.executions = 0;
    this.priority = EventPriority.NORMAL;
    this.ignoreCancelled = true;
  }

  /**
   * Filters the contents of the Event.
   *
   * @param predicate Predicate of event
   * @return EventWaiter
   */
  public EventWaiter<E> filter(final Predicate<E> predicate) {
    filter = filter.and(predicate);
    return this;
  }

  /**
   * Expires after that number of executions
   *
   * @param executions Integer
   * @return EventWaiter
   */
  public EventWaiter<E> expire(final int executions) {
    this.expireAfterExecute = true;
    this.executions = executions;
    return this;
  }

  /**
   * Sets the EventPriority of the EventWaiter
   *
   * @param priority EventPriority
   * @return EventWaiter
   */
  public EventWaiter<E> priority(final EventPriority priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Handler for the EventWaiter
   *
   * @param consumer Consumer of the event
   * @return EventWaiter
   */
  public EventWaiter<E> handler(final Consumer<E> consumer) {
    if (action != null) {
      action = action.andThen(consumer);
      return this;
    }

    action = consumer;
    return this;
  }

  /**
   * Registers EventWaiter as a listener.
   *
   * @param plugin Plugin
   */
  public void build(final Plugin plugin) {
    final EventListener<E> executor = new EventListener<>(this);
    Bukkit.getPluginManager()
        .registerEvent(event, executor, priority, executor, plugin, ignoreCancelled);
  }

  /**
   * Cancels the event.
   *
   * @return EventWaiter
   */
  public EventWaiter<E> cancel() {
    return handler(event -> {
      if (event instanceof Cancellable) {
        ((Cancellable) event).setCancelled(true);
      }
    });
  }

}
