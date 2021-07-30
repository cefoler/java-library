package com.celeste.library.core.registry.structure.splitter.impl;

import com.celeste.library.core.registry.structure.entry.Entry;
import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.structure.nodes.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public final class EntrySplitter<K, V> extends MapSplitter<K, V>
    implements Spliterator<Entry<K, V>> {

  public EntrySplitter(final MapRegistry<K, V> map, final Node<K, V> current,
                       final int index, final int fence, final int estimatedSize,
                       final int expectedModificationsCount
  ) {
    super(map, current, index, fence, estimatedSize, expectedModificationsCount);
  }

  @Override
  public EntrySplitter<K, V> trySplit() {
    int value = getTableSize(), lo = index, mid = (lo + value) >>> 1;
    return (lo >= mid || current != null)
        ? new EntrySplitter<>(map, current, lo, index = mid, size >>>= 1, modificationsCount)
        : null;
  }

  @Override
  public void forEachRemaining(@NotNull Consumer<? super Entry<K, V>> action) {
    final Node<K, V>[] nodes = map.getNodes();
    final int value = getTableSize();

    if (value < 0) {
      length = (nodes == null) ? 0 : nodes.length;
    }

    int index = 0;
    if (nodes == null || nodes.length < value && (index = this.index) < 0) {
      return;
    }

    Node<K, V> node = current;
    current = null;

    do {
      if (node == null) {
        node = nodes[index++];
        continue;
      }

      action.accept(node);
      node = node.getNext();
    } while (node != null || index < value);
  }

  @Override
  public boolean tryAdvance(@NotNull final Consumer<? super Entry<K, V>> action) {
    final Node<K, V>[] table = map.getNodes();
    final int value = getTableSize();

    if (table == null || table.length < value && index < 0) {
      return false;
    }

    while (current != null || index < value) {
      if (current == null) {
        current = table[index++];
        continue;
      }

      final Node<K, V> node = current;

      current = current.getNext();
      action.accept(node);
      return true;
    }

    return false;
  }

  @Override
  public int characteristics() {
    return (length < 0 || size == map.getSize() ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
  }

}
