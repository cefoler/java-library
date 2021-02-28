package com.celeste.registries;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentRegistry<K, V> {

    private final Map<K, V> map;

    public ConcurrentRegistry() {
        this.map = new ConcurrentHashMap<>();
    }

    public void register(K k, V v) {
        map.put(k, v);
    }

    public V getByValue(K k) {
        return map.get(k);
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public void remove(K k) {
        map.remove(k);
    }

    public Set<Map.Entry<K, V>> getKeys() {
        return map.entrySet();
    }

    public Collection<V> getAll() {
        return map.values();
    }

    private void wipe() {
        map.clear();
    }

}
