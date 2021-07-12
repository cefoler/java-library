package com.celeste.library.core.model.registry.test.iterator.impl;

import com.celeste.library.core.model.registry.test.Entry;
import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.iterator.HashIterator;

import java.util.Iterator;

public final class EntryIterator<K, V> extends HashIterator<K, V> implements Iterator<Entry<K, V>> {

  public EntryIterator(final MapRegistry<K, V> registry) {
    super(registry);
  }

  public final Entry<K, V> next() {
    return nextNode();
  }

}