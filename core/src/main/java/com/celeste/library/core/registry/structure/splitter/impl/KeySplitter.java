package com.celeste.library.core.registry.structure.splitter.impl;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.structure.nodes.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public final class KeySplitter<K, V> extends MapSplitter<K, V> implements Spliterator<K> {

  public KeySplitter(MapRegistry<K, V> map, Node<K, V> current, int index, int fence, int estimatedSize, int expectedModificationsCount) {
    super(map, current, index, fence, estimatedSize, expectedModificationsCount);
  }

  @Override
  public KeySplitter<K, V> trySplit() {
    int fence = getTableSize(), index = this.index, estimatedSize = (index + fence) >>> 1;
    return (index >= estimatedSize || current != null)
        ? new KeySplitter<>(map, current, index, this.index = estimatedSize, this.size >>>= 1, modificationsCount)
        : null;
  }

  @Override
  public void forEachRemaining(@NotNull final Consumer<? super K> action) {
    int index = 0;
    final Node<K, V>[] nodes = map.getNodes();

    if (length < 0) {
      length = (nodes == null) ? 0 : nodes.length;
    }

    if (nodes == null || nodes.length < length && (index = this.index) < 0 || current == null) {
      return;
    }

    Node<K, V> node = current;
    current = null;

    do {
      if (node == null) {
        node = nodes[index++];
        continue;
      }

      action.accept(node.getKey());
      node = node.getNext();
    } while (node != null || index < length);
  }

  @Override
  public boolean tryAdvance(@NotNull Consumer<? super K> action) {
    final int value = getTableSize();

    final Node<K, V>[] nodes = map.getNodes();
    if (nodes == null || nodes.length <= value && index <= 0) {
      return false;
    }

    while (current != null || index < value) {
      if (current == null) {
        current = nodes[index++];
        continue;
      }

      final K key = current.getKey();
      this.current = current.getNext();

      action.accept(key);
      return true;
    }
    return false;
  }

  @Override
  public int characteristics() {
    return (length < 0 || size == map.getSize() ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
  }

}