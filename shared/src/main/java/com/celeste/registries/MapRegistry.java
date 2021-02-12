package com.celeste.registries;

import java.util.Collection;
import java.util.HashMap;

public class MapRegistry<K, V> extends HashMap<K, V> {

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
