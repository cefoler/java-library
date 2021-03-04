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
 *
 * @param <E> Event class
 */
public final class EventWaiter<E extends Event> {

    private final Class<E> clazz;
    private Predicate<E> filter;

    private Consumer<E> action;

    private boolean expireAfterExecute;
    private int executions;

    private EventPriority priority;
    private boolean ignoreCancelled;

    /**
     * Constructor for the EventSubscription
     *
     * @param clazz Event class.
     */
    private EventWaiter(final Class<E> clazz) {
        this.clazz = clazz;
        this.filter = Objects::nonNull;
        this.expireAfterExecute = false;
        this.executions = -1;
        this.priority = EventPriority.NORMAL;
        this.ignoreCancelled = false;
    }

    /**
     * Creates the EventWaiter with that Event class.
     *
     * @param clazz Event class
     * @param <T> Event class
     *
     * @return Event class
     */
    public static <T extends Event> EventWaiter<T> of(final Class<T> clazz) {
        return new EventWaiter<>(clazz);
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
    public EventWaiter<E> expireAfterExecute(final int executions) {
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
     * Boolean for ignoreCancelled of the Event.
     *
     * @param ignoreCancelled boolean
     * @return EventWaiter
     */
    public EventWaiter<E> ignoreCancelled(final boolean ignoreCancelled) {
        this.ignoreCancelled = ignoreCancelled;
        return this;
    }


    /**
     * Handler for the EventWaiter
     *
     * @param consumer Consumer of the event
     * @return EventWaiter
     */
    public EventWaiter<E> handler(final Consumer<E> consumer) {
        if (action == null) {
            action = consumer;
        } else {
            action = action.andThen(consumer);
        }

        return this;
    }

    /**
     * Cancels the event.
     *
     * @return EventWaiter
     */
    public EventWaiter<E> cancel() {
        return handler(e -> {
            if (e instanceof Cancellable) {
                ((Cancellable) e).setCancelled(true);
            }
        });
    }

    /**
     * Registers EventWaiter as a listener.
     *
     * @param plugin Plugin
     */
    public void listen(final Plugin plugin) {
        final EventListenerExecutor executor = new EventListenerExecutor<>(this);
        Bukkit.getPluginManager().registerEvent(clazz, executor, priority, executor, plugin, ignoreCancelled);
    }

    @AllArgsConstructor
    private static class EventListenerExecutor<I extends Event> implements Listener, EventExecutor {

        private final EventWaiter<I> builder;

        @Override
        public void execute(@NotNull final Listener listener, @NotNull final Event eventOne) {
            if (!builder.clazz.isInstance(eventOne)) {
                return;
            }

            final I event = builder.clazz.cast(eventOne);
            if (builder.filter.negate().test(event)) {
                return;
            }

            final Consumer<I> action = builder.action;
            if (action != null) {
                action.accept(event);
            }

            if (builder.expireAfterExecute) {
                if (builder.executions > 1) {
                    builder.executions--;
                } else {
                    unregister();
                }
            }

        }

        public void unregister() {
            for (HandlerList handlerList : HandlerList.getHandlerLists()) {
                handlerList.unregister(this);
            }
        }

    }

}
