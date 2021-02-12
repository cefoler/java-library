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

@SuppressWarnings("unchecked")
public class EventFilters {

    private static final Predicate<? extends Cancellable> CANCELLED = Cancellable::isCancelled;
    private static final Predicate<? extends PlayerMoveEvent> SAME_WORLD = e -> isSameWorld(e.getFrom(), e.getTo());
    private static final Predicate<? extends PlayerMoveEvent> SAME_BLOCK = e -> isSameBlock(e.getFrom(), e.getTo());
    private static final Predicate<? extends PlayerMoveEvent> SAME_CHUNK = e -> isSameChunk(e.getFrom(), e.getTo());
    private static final Predicate<? extends AsyncPlayerPreLoginEvent> LOGIN_ALLOWED = e -> e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED;

    public static <E extends Cancellable> Predicate<E> ignoreCancelled() {
        return (Predicate<E>) CANCELLED.negate();
    }

    public static <E extends PlayerMoveEvent> Predicate<E> sameBlock() {
        return (Predicate<E>) SAME_BLOCK;
    }

    public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameBlock() {
        return (Predicate<E>) sameBlock().negate();
    }

    public static <E extends PlayerMoveEvent> Predicate<E> sameChunk() {
        return (Predicate<E>) SAME_CHUNK;
    }

    public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameChunk() {
        return (Predicate<E>) sameChunk().negate();
    }

    public static <E extends PlayerMoveEvent> Predicate<E> sameWorld() {
        return (Predicate<E>) SAME_WORLD;
    }

    public static <E extends PlayerMoveEvent> Predicate<E> ignoreIfSameWorld() {
        return (Predicate<E>) sameWorld().negate();
    }

    public static <E extends AsyncPlayerPreLoginEvent> Predicate<E> loginAllowed() {
        return (Predicate<E>) LOGIN_ALLOWED;
    }

    public static <E extends AsyncPlayerPreLoginEvent> Predicate<E> ignoreIfDisallowedLogin() {
        return (Predicate<E>) loginAllowed().negate();
    }

    public static <E extends EntityEvent> Predicate<E> entityHasMetadata(String metadataKey) {
        return e -> e.getEntity().hasMetadata(metadataKey);
    }

    public static <E extends PlayerEvent> Predicate<E> playerEqualsTo(Player player) {
        return e -> e.getPlayer().getUniqueId().equals(player.getUniqueId());
    }

    public static <E extends PlayerEvent> Predicate<E> playerHasPermission(String permission) {
        return e -> e.getPlayer().hasPermission(permission);
    }

    public static <E extends PlayerEvent> Predicate<E> playerFilter(Predicate<Player> filter) {
        return e -> filter.test(e.getPlayer());
    }

    public static <E extends AsyncPlayerChatEvent> Predicate<E> messageFilter(Predicate<String> filter) {
        return e -> filter.test(e.getMessage());
    }


    private static boolean isSameChunk(Location from, Location to) {
        if (!isSameWorld(from, to)) {
            return false;
        }

        int fromX = from.getBlockX();
        int fromZ = from.getBlockZ();

        int toX = to.getBlockX();
        int toZ = to.getBlockZ();

        return (fromX >> 4) == (fromX >> 4) && (fromZ >> 4) == (toZ >> 4);
    }

    private static boolean isSameBlock(Location from, Location to) {
        if (!isSameWorld(from, to)) {
            return false;
        }

        int fromBlockX = from.getBlockX();
        int fromBlockY = from.getBlockY();
        int fromBlockZ = from.getBlockZ();

        int toBlockX = to.getBlockX();
        int toBlockY = to.getBlockY();
        int toBlockZ = to.getBlockZ();

        return fromBlockX == toBlockX && fromBlockY == toBlockY && fromBlockZ == toBlockZ;
    }

    private static boolean isSameWorld(Location from, Location to) {
        return from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName());
    }
}