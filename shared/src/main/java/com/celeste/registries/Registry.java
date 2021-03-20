package com.celeste.registries;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class Registry<T, U> implements Serializable, Cloneable {

    private final Map<T, U> map;

    /**
     * Register into map
     *
     * @param key T
     * @param value U
     */
    public void register(@NotNull final T key, @NotNull final U value) {
        map.put(key, value);
    }

    /**
     * Register if key is absent in the map
     *
     * @param key T
     * @param value U
     */
    public void registerIfAbsent(@NotNull final T key, @NotNull final U value) {
        map.putIfAbsent(key, value);
    }

    /**
     * Registers all into the map
     *
     * @param values Map<T, U>
     */
    public void registerAll(@NotNull final Map<T, U> values) {
        map.putAll(values);
    }

    /**
     * Removes from the map
     *
     * @param key T
     */
    public void remove(@NotNull final T key) {
        map.remove(key);
    }

    /**
     * Replaces in the map
     *
     * @param key T
     * @param value U
     */
    public void replace(@NotNull final T key, @NotNull final U value) {
        map.replace(key, value);
    }

    /**
     * Gets object from the map
     *
     * @param key T
     * @return Object U
     */
    @NotNull
    public U get(@NotNull final T key) {
        return map.get(key);
    }

    /**
     * Check if that key is on the map
     *
     * @param key T
     * @return boolean true if contains
     */
    public boolean contains(@NotNull final T key) {
        return map.containsKey(key);
    }

    /**
     * @return Map key set
     */
    @NotNull
    public Set<T> getKeys() {
        return map.keySet();
    }

    /**
     * @return Map entry set
     */
    public Set<Map.Entry<T, U>> getEntrySet() {
        return map.entrySet();
    }

    /**
     * Get values from map
     * @return Collection
     */
    @NotNull
    public Collection<U> getAll() {
        return map.values();
    }

    /**
     * Sort the map by the Comparator, returns a List
     *
     * @param comparator Comparator
     * @return List
     */
    public List<U> sort(final Comparator<? super U> comparator) {
        return getAll()
            .stream()
            .sorted(comparator)
            .collect(Collectors.toList());
    }

    /**
     * @return Size of the map
     */
    public int size() {
        return map.size();
    }

    /**
     * Check if the map is empty
     * @return true if empty
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Wipe all information from the map
     */
    public void wipe() {
        map.clear();
    }

    /**
     * @return Map
     */
    @NotNull
    public Map<T, U> getMap() {
        return map;
    }

}
