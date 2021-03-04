package com.celeste.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.function.Predicate;

/**
 * EventFilters that can be used along with the EventWaiter
 */
public final class EventFilters {

    private static final Predicate<? extends Cancellable> CANCELLED = Cancellable::isCancelled;
    private static final Predicate<? extends PlayerMoveEvent> SAME_WORLD = e -> isSameWorld(e.getFrom(), e.getTo());
    private static final Predicate<? extends PlayerMoveEvent> SAME_BLOCK = e -> isSameBlock(e.getFrom(), e.getTo());
    private static final Predicate<? extends PlayerMoveEvent> SAME_CHUNK = e -> isSameChunk(e.getFrom(), e.getTo());
    private static final Predicate<? extends AsyncPlayerPreLoginEvent> LOGIN_ALLOWED = e ->
        e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED;

    private EventFilters() {}

    /**
     * Ignore if cancelled.
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends Cancellable> Predicate<E> ignoreCancelled() {
        return (Predicate<E>) CANCELLED.negate();
    }

    /**
     * Checks if it is the same block
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends PlayerMoveEvent> Predicate<E> sameBlock() {
        return (Predicate<E>) SAME_BLOCK;
    }

    /**
     * Ignores if it's the same block
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameBlock() {
        return (Predicate<E>) sameBlock().negate();
    }

    /**
     * Checks if it is the same chunk
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends PlayerMoveEvent> Predicate<E> sameChunk() {
        return (Predicate<E>) SAME_CHUNK;
    }

    /**
     * Ignores if it's the same chunk
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameChunk() {
        return (Predicate<E>) sameChunk().negate();
    }

    /**
     * Checks if it is the same world
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends PlayerMoveEvent> Predicate<E> sameWorld() {
        return (Predicate<E>) SAME_WORLD;
    }

    /**
     * Ignores if it's the same world
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameWorld() {
        return (Predicate<E>) sameWorld().negate();
    }

    /**
     * Checks if the login is allowed
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends AsyncPlayerPreLoginEvent> Predicate<E> loginAllowed() {
        return (Predicate<E>) LOGIN_ALLOWED;
    }

    /**
     * Ignores if the login is not allowed.
     *
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends AsyncPlayerPreLoginEvent> Predicate<E> ignoreIfDisallowedLogin() {
        return (Predicate<E>) loginAllowed().negate();
    }

    /**
     * Checks if the entity has metadata.
     *
     * @param <E> Predicate
     * @param metadataKey Key for the metadata
     *
     * @return Predicate
     */
    public static <E extends EntityEvent> Predicate<E> entityHasMetadata(final String metadataKey) {
        return e -> e.getEntity().hasMetadata(metadataKey);
    }

    /**
     * Checks if player equals to target
     *
     * @param player Player
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends PlayerEvent> Predicate<E> playerEqualsTo(final Player player) {
        return e -> e.getPlayer().getUniqueId().equals(player.getUniqueId());
    }

    /**
     * Checks if player has permission
     *
     * @param permission String of the permission
     * @param <E> Predicate
     * @return Predicate
     */
    public static <E extends PlayerEvent> Predicate<E> playerHasPermission(final String permission) {
        return e -> e.getPlayer().hasPermission(permission);
    }

    /**
     * Filter if it's not the player
     *
     * @param filter Players
     * @param <E> Predicate
     *
     * @return Predicate
     */
    public static <E extends PlayerEvent> Predicate<E> playerFilter(final Predicate<Player> filter) {
        return e -> filter.test(e.getPlayer());
    }

    /**
     * Filter if it's not those messages
     *
     * @param filter String of the messages
     * @param <E> Predicate
     *
     * @return Predicate
     */
    public static <E extends AsyncPlayerChatEvent> Predicate<E> messageFilter(final Predicate<String> filter) {
        return e -> filter.test(e.getMessage());
    }

    private static boolean isSameChunk(final Location from, final Location to) {
        if (!isSameWorld(from, to)) {
            return false;
        }

        int fromX = from.getBlockX();
        int fromZ = from.getBlockZ();

        int toZ = to.getBlockZ();

        return (fromX >> 4) == (fromX >> 4) && (fromZ >> 4) == (toZ >> 4);
    }

    private static boolean isSameBlock(final Location first, final Location target) {
        if (!isSameWorld(first, target)) {
            return false;
        }

        int fromBlockX = first.getBlockX();
        int fromBlockY = first.getBlockY();
        int fromBlockZ = first.getBlockZ();

        int toBlockX = target.getBlockX();
        int toBlockY = target.getBlockY();
        int toBlockZ = target.getBlockZ();

        return fromBlockX == toBlockX && fromBlockY == toBlockY && fromBlockZ == toBlockZ;
    }

    /**
     * Method to check if it is the same world.
     *
     * @param first Location
     * @param target Location
     *
     * @return boolean true if it's the same
     */
    private static boolean isSameWorld(final Location first, final Location target) {
        return first.getWorld().getName().equalsIgnoreCase(target.getWorld().getName());
    }

}
