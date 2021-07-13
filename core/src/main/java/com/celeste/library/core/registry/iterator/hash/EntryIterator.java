package com.celeste.library.core.registry.iterator.hash;

import com.celeste.library.core.registry.entry.Entry;
import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.iterator.HashIterator;

import java.util.Iterator;

public final class EntryIterator<K, V> extends HashIterator<K, V> implements Iterator<Entry<K, V>> {

  public EntryIterator(final MapRegistry<K, V> registry) {
    super(registry);
  }

  public final Entry<K, V> next() {
    return nextNode();
  }

}