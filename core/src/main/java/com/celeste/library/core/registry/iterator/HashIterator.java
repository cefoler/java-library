package com.celeste.library.core.registry.iterator;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.nodes.Node;

import java.util.NoSuchElementException;

public abstract class HashIterator<K, V> {

  private final MapRegistry<K, V> registry;

  public Node<K, V> next;
  public Node<K, V> current;
  private int index;

  public HashIterator(final MapRegistry<K, V> registry) {
    this.registry = registry;

    final Node<K, V>[] node = registry.getNodes();
    current = next = null;
    index = 0;
    if (node != null && registry.getSize() > 0) {
      do {
      } while (index < node.length && (next = node[index++]) == null);
    }
  }

  public final boolean hasNext() {
    return next != null;
  }

  protected final Node<K, V> nextNode() {
    Node<K, V>[] nodes = registry.getNodes();
    final Node<K, V> node = next;
    if (node == null) {
      throw new NoSuchElementException();
    }

    if ((next = (current = node).getNext()) == null && nodes != null) {
      do {
      } while (index < nodes.length && (next = nodes[index++]) == null);
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