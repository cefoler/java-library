package com.celeste.registries;

import java.util.Collection;
import java.util.TreeMap;

public class TreeMapRegistry<K, V> extends TreeMap<K, V> {

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
