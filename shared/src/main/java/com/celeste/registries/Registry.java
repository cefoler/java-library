package com.celeste.registries;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public abstract class Registry<T, U> implements Serializable, Cloneable {

    private final Map<T, U> map;

    public void register(@NotNull final T key, @NotNull final U value) {
        map.put(key, value);
    }

    public void registerIfAbsent(@NotNull final T key, @NotNull final U value) {
        map.putIfAbsent(key, value);
    }

    public void registerAll(@NotNull final Map<T, U> values) {
        map.putAll(values);
    }

    public void remove(@NotNull final T key) {
        map.remove(key);
    }

    @NotNull
    public U get(@NotNull final T key) {
        return map.get(key);
    }

    public boolean contains(@NotNull final T key) {
        return map.containsKey(key);
    }

    @NotNull
    public Set<T> getKeys() {
        return map.keySet();
    }

    @NotNull
    public Collection<U> getAll() {
        return map.values();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void wipe() {
        map.clear();
    }

    @NotNull
    public Map<T, U> getMap() {
        return map;
    }

}
