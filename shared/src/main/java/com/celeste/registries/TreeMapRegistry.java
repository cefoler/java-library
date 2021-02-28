package com.celeste.registries;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TreeMapRegistry<K, V> {

    private final Map<K, V> map;

    public TreeMapRegistry() {
        this.map = new TreeMap<>();
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

    public Set<Map.Entry<K, V>> getKeys() {
        return map.entrySet();
    }

    public void remove(K k) {
        map.remove(k);
    }

    public Collection<V> getAll() {
        return map.values();
    }

    private void wipe() {
        map.clear();
    }

}
