package com.celeste.library.core.model.registry.test.iterator.hash;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.iterator.HashIterator;

import java.util.Iterator;

public final class KeyIterator<K, V> extends HashIterator<K, V> implements Iterator<K> {

  public KeyIterator(final MapRegistry<K, V> registry) {
    super(registry);
  }

  public final K next() {
    return nextNode().getKey();
  }

}
