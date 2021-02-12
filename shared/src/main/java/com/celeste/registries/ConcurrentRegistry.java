package com.celeste.registries;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentRegistry<K, V> extends ConcurrentHashMap<K, V> {

    public void register(K k, V v) {
        put(k, v);
    }

    public V getByValue(K k) {
        return get(k);
    }

    public Collection<V> getAll() {
        return values();
    }

    private void wipeAll() {
        clear();
    }

}
