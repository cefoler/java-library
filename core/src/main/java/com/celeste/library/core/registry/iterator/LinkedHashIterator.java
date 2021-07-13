package com.celeste.library.core.registry.iterator;

import com.celeste.library.core.registry.impl.LinkedRegistry;
import com.celeste.library.core.registry.nodes.Node;
import com.celeste.library.core.registry.nodes.impl.LinkedNode;
import lombok.Getter;
import lombok.Setter;

import java.util.NoSuchElementException;

import static com.celeste.library.core.registry.impl.MapRegistry.hash;

@Getter
@Setter
public abstract class LinkedHashIterator<K, V> {

  private final LinkedRegistry<K, V> registry;

  private LinkedNode<K,V> next;
  private LinkedNode<K,V> current;

  public LinkedHashIterator(final LinkedRegistry<K, V> registry) {
    this.registry = registry;

    next = registry.getEldest();
    current = null;
  }

  public final boolean hasNext() {
    return next != null;
  }

  public final LinkedNode<K,V> nextNode() {
    final LinkedNode<K,V> node = next;
    if (node == null) {
      throw new NoSuchElementException();
    }

    setCurrent(node);
    setNext(node.getAfter());
    return node;
  }

  public final void remove() {
    final Node<K,V> node = current;
    if (node == null) {
      throw new IllegalStateException();
    }

    current = null;

    final K key = node.getKey();
    registry.removeNode(hash(key), key, null, false, false);
  }

}
