package com.celeste.registries;

import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class WeakRegistry<K, V> {

    @Getter
    private final Map<K, V> map;

    public WeakRegistry() {
        this.map = new WeakHashMap<>();
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

    public int size() {
        return map.size();
    }

    public void remove(K k) {
        map.remove(k);
    }

    public Collection<V> getAll() {
        return map.values();
    }

    private void wipeAll() {
        map.clear();
    }

}
