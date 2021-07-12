package com.celeste.library.core.model.registry.test.iterator.linked;

import com.celeste.library.core.model.registry.test.impl.LinkedRegistry;
import com.celeste.library.core.model.registry.test.iterator.LinkedHashIterator;

import java.util.Iterator;

public final class LinkedKeyIterator<K, V> extends LinkedHashIterator<K, V> implements Iterator<K> {

  public LinkedKeyIterator(final LinkedRegistry<K, V> registry) {
    super(registry);
  }

  public final K next() { return nextNode().getKey(); }

}
