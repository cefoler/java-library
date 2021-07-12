package com.celeste.library.core.model.registry.test.iterator.impl;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.iterator.HashIterator;

import java.util.Iterator;

public final class ValueIterator<K, V> extends HashIterator<K, V> implements Iterator<V> {

  public ValueIterator(MapRegistry<K, V> registry) {
    super(registry);
  }

  public final V next() {
    return nextNode().getValue();
  }

}
