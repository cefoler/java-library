package com.celeste.library.core.registry.iterator.hash;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.iterator.HashIterator;

import java.util.Iterator;

public final class KeyIterator<K, V> extends HashIterator<K, V> implements Iterator<K> {

  public KeyIterator(final MapRegistry<K, V> registry) {
    super(registry);
  }

  public final K next() {
    return nextNode().getKey();
  }

}
