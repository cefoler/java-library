package com.celeste.library.core.registry.structure.iterator.hash;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.structure.iterator.HashIterator;

import java.util.Iterator;

public final class KeyIterator<K, V> extends HashIterator<K, V> implements Iterator<K> {

  public KeyIterator(final MapRegistry<K, V> registry) {
    super(registry);
  }

  public K next() {
    return nextNode().getKey();
  }

}
