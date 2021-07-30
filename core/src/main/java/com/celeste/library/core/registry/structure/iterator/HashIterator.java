package com.celeste.library.core.registry.structure.iterator;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.structure.nodes.Node;

import java.util.HashMap;
import java.util.NoSuchElementException;

public abstract class HashIterator<K, V> {

  private final MapRegistry<K, V> registry;

  public Node<K, V> next;
  public Node<K, V> current;

  private int index;

  public HashIterator(final MapRegistry<K, V> registry) {
    this.registry = registry;

    final Node<K, V>[] node = registry.getNodes();
    this.current = this.next = null;
    this.index = 0;

    if (node != null && registry.getSize() > 0) {
      next = node[index++];
    }
  }

  public final boolean hasNext() {
    return next != null;
  }

  protected final Node<K, V> nextNode() {
    final Node<K, V>[] nodes = registry.getNodes();

    final Node<K, V> node = next;
    if (node == null) {
      throw new NoSuchElementException();
    }

    if ((next = (current = node).getNext()) == null && nodes != null) {
      this.next = nodes[index++];
    }

    return node;
  }

  public final void remove() {
    final Node<K, V> node = current;
    if (node == null) {
      throw new UnsupportedOperationException();
    }

    current = null;

    final K key = node.getKey();
    registry.removeNode(MapRegistry.hash(key), key, null, false, false);
  }

}