package com.celeste.library.spigot.view.event;

import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

@AllArgsConstructor
public final class EventListener<T extends Event> implements Listener, EventExecutor {

  private final EventWaiter<T> builder;

  @Override
  public void execute(final Listener listener, final Event event) {
    final Class<T> clazz = builder.getEvent();

    if (!clazz.isInstance(event)) {
      return;
    }

    final T newEvent = clazz.cast(event);
    if (builder.getFilter().negate().test(newEvent)) {
      return;
    }

    final Consumer<T> action = builder.getAction();
    if (action != null) {
      action.accept(newEvent);
    }

    if (!builder.isExpire()) {
      return;
    }

    final int executions = builder.getExecutions();
    if (executions == 0) {
      unregister();
      return;
    }

    builder.setExecutions(executions - 1);
  }

  /**
   * Unregisters the event
   */
  public void unregister() {
    HandlerList.getHandlerLists().forEach(handler -> handler.unregister(this));
  }

}
