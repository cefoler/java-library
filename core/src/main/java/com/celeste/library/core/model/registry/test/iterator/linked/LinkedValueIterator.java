package com.celeste.library.core.model.registry.test.iterator.linked;

import com.celeste.library.core.model.registry.test.impl.LinkedRegistry;
import com.celeste.library.core.model.registry.test.iterator.LinkedHashIterator;

import java.util.Iterator;

public final class LinkedValueIterator<K, V> extends LinkedHashIterator<K, V> implements Iterator<V> {

  public LinkedValueIterator(final LinkedRegistry<K, V> registry) {
    super(registry);
  }

  public final V next() { return nextNode().getValue(); }

}
