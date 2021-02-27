package com.celeste.event;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This class is used to trigger when the specific Event is triggered by Bukkit.
 */
public class EventWaiter<E extends Event> {

    private final Class<E> clazz;
    private Predicate<E> filter;

    private Consumer<E> action;

    private boolean expireAfterExecute;
    private int executions;

    private EventPriority priority;
    private boolean ignoreCancelled;

    /**
     * Constructor for the EventSubscription
     * @param clazz Event class.
     */
    private EventWaiter(Class<E> clazz) {
        this.clazz = clazz;
        this.filter = Objects::nonNull;
        this.expireAfterExecute = false;
        this.executions = -1;
        this.priority = EventPriority.NORMAL;
        this.ignoreCancelled = false;
    }

    public static <T extends Event> EventWaiter<T> of(Class<T> clazz) {
        return new EventWaiter<>(clazz);
    }

    /**
     * Filters the contents of the Event.
     */
    public EventWaiter<E> filter(Predicate<E> predicate) {
        filter = filter.and(predicate);
        return this;
    }

    public EventWaiter<E> expireAfterExecute(int executions) {
        this.expireAfterExecute = true;
        this.executions = executions;
        return this;
    }

    public EventWaiter<E> priority(EventPriority priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Boolean for ignoreCancelled of the Event.
     */
    public EventWaiter<E> ignoreCancelled(boolean ignoreCancelled) {
        this.ignoreCancelled = ignoreCancelled;
        return this;
    }


    public EventWaiter<E> handler(Consumer<E> consumer) {
        if (action == null) {
            action = consumer;
        } else {
            action = action.andThen(consumer);
        }

        return this;
    }

    /**
     * Cancel event.
     */
    public EventWaiter<E> cancel() {
        return handler(e -> {
            if (e instanceof Cancellable) {
                ((Cancellable) e).setCancelled(true);
            }
        });
    }

    public void listen(Plugin plugin) {
        final EventListenerExecutor executor = new EventListenerExecutor<>(this);
        Bukkit.getPluginManager().registerEvent(clazz, executor, priority, executor, plugin, ignoreCancelled);
    }

    @AllArgsConstructor
    private static class EventListenerExecutor<I extends Event> implements Listener, EventExecutor {

        private final EventWaiter<I> builder;

        @Override
        public void execute(@NotNull Listener listener, @NotNull Event e) {
            if (!builder.clazz.isInstance(e)) return;

            final I event = builder.clazz.cast(e);
            if (builder.filter.negate().test(event)) return;

            final Consumer<I> action = builder.action;
            if (action != null) action.accept(event);

            if (builder.expireAfterExecute) {
                if (builder.executions > 1) builder.executions--;
                else unregister();
            }

        }

        public void unregister() {
            for (HandlerList handlerList : HandlerList.getHandlerLists()) handlerList.unregister(this);
        }

    }

}