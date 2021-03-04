package com.celeste.registries;

import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ConcurrentRegistry<K, V> {

    @Getter
    private final Map<K, V> map;

    /**
     * Constructor generating a ConcurrentHashMap
     */
    public ConcurrentRegistry() {
        this.map = new ConcurrentHashMap<>();
    }

    /**
     * Registers into the map
     *
     * @param k Key
     * @param v Value
     */
    public void register(final K k, final V v) {
        map.put(k, v);
    }

    /**
     * Gets by Key
     * @param k Key
     *
     * @return Value
     */
    public V getByValue(final K k) {
        return map.get(k);
    }

    /**
     * @param key Key
     *
     * @return boolean If contains key
     */
    public boolean containsKey(final K key) {
        return map.containsKey(key);
    }

    /**
     * Removes value that has the specified Key from the map
     *
     * @param k Key
     */
    public void remove(final K k) {
        map.remove(k);
    }

    /**
     * @return Size of the map
     */
    public int size() {
       return map.size();
    }

    /**
     * @return Set with K, V of all map.
     */
    public Set<Map.Entry<K, V>> getKeys() {
        return map.entrySet();
    }

    /**
     * @return Collection with all values of the map
     */
    public Collection<V> getAll() {
        return map.values();
    }

    /**
     * Wipe all values from the map.
     */
    private void wipe() {
        map.clear();
    }

}
