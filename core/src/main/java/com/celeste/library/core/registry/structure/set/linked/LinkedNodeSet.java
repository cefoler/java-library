package com.celeste.library.core.registry.structure.set.linked;

import com.celeste.library.core.registry.impl.LinkedRegistry;
import com.celeste.library.core.registry.structure.iterator.linked.LinkedNodeIterator;
import com.celeste.library.core.registry.structure.nodes.Node;
import com.celeste.library.core.registry.structure.nodes.impl.LinkedNode;
import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.util.Wrapper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@AllArgsConstructor
public final class LinkedNodeSet<K, V> extends AbstractSet<LinkedNode<K, V>> {

  private final LinkedRegistry<K, V> registry;

  public final int size() {
    return registry.size();
  }

  public final void clear() {
    registry.wipe();
  }

  public final @NotNull Iterator<LinkedNode<K,V>> iterator() {
    return new LinkedNodeIterator<>(registry);
  }

  public final boolean contains(final Object object) {
    if (!Wrapper.isObject(object, LinkedNode.class)) {
      return false;
    }

    final LinkedNode<?, ?> node = (LinkedNode<?, ?>) object;
    final Object key = node.getKey();

    final Node<K,V> candidate = registry.getNode(MapRegistry.hash(key), key);
    return candidate != null && candidate.equals(node);
  }

  public final boolean remove(final Object object) {
    if (!Wrapper.isObject(object, LinkedNode.class)) {
      return false;
    }

    final LinkedNode<?,?> node = (LinkedNode<?,?>) object;
    final Object key = node.getKey();

    return registry.removeNode(MapRegistry.hash(key), key, node.getValue(), true, true) != null;
  }

  public final Spliterator<LinkedNode<K,V>> spliterator() {
    return Spliterators.spliterator(this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT);
  }

  public final void forEach(@NotNull final Consumer<? super LinkedNode<K,V>> action) {
    for (LinkedNode<K,V> node = registry.getEldest(); node != null; node = node.getAfter()) {
      action.accept(node);
    }
  }

}
