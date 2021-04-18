package com.celeste.library.spigot.view.event;

import com.celeste.library.spigot.view.event.wrapper.AbstractEventWrapper;
import lombok.AllArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor
public final class EventListener<E extends AbstractEventWrapper> implements Listener, EventExecutor {

    private final EventWaiter<E> builder;

    @Override
    public void execute(@NotNull final Listener listener, @NotNull final Event eventOne) {
        if (!builder.getEvent().isInstance(eventOne)) return;

        final E event = builder.getEvent().cast(eventOne);
        if (builder.getFilter().negate().test(event)) return;

        final Consumer<E> action = builder.getAction();
        if (action != null) action.accept(event);

        if (builder.isExpireAfterExecute()) {
           if (builder.getExecutions() == 0) unregister();
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
