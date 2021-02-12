package com.celeste.registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LinkedRegistry<K, V> extends LinkedHashMap<K, V> {

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
