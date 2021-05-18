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
  public void execute(final Listener listener, final Event eventOne) {
    if (!builder.getEvent().isInstance(eventOne)) {
      return;
    }

    final T event = builder.getEvent().cast(eventOne);
    if (builder.getFilter().negate().test(event)) {
      return;
    }

    final Consumer<T> action = builder.getAction();
    if (action != null) {
      action.accept(event);
    }

    if (builder.isExpireAfterExecute()) {
      if (builder.getExecutions() == 0) {
        unregister();
        return;
      }

      builder.setExecutions(builder.getExecutions() - 1);
    }
  }

  /**
   * Unregisters the event
   */
  public void unregister() {
    for (HandlerList handlerList : HandlerList.getHandlerLists()) {
      handlerList.unregister(this);
    }
  }

}
