package com.celeste.library.core.registry.structure.iterator.linked;

import com.celeste.library.core.registry.impl.LinkedRegistry;
import com.celeste.library.core.registry.structure.iterator.LinkedHashIterator;
import com.celeste.library.core.registry.structure.nodes.impl.LinkedNode;

import java.util.Iterator;

public final class LinkedNodeIterator<K, V> extends LinkedHashIterator<K, V> implements Iterator<LinkedNode<K,V>> {

  public LinkedNodeIterator(final LinkedRegistry<K, V> registry) {
    super(registry);
  }

  public final LinkedNode<K,V> next() { return nextNode(); }

}
