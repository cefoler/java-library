package com.celeste.library.core.model.registry.test.splitter;

import com.celeste.library.core.model.registry.test.Entry;
import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.nodes.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public final class EntrySpliterator<K, V> extends MapSpliterator<K, V> implements Spliterator<Entry<K, V>> {

  public EntrySpliterator(final MapRegistry<K, V> map, final Node<K, V> current,
                          final int index, final int fence, final int estimatedSize,
                          final int expectedModificationsCount
  ) {
    super(map, current, index, fence, estimatedSize, expectedModificationsCount);
  }

  public EntrySpliterator<K, V> trySplit() {
    int value = getFence(), lo = index, mid = (lo + value) >>> 1;
    return (lo >= mid || current != null)
        ? new EntrySpliterator<>(map, current, lo, index = mid, estimatedSize >>>= 1, expectedModificationsCount)
        : null;
  }

  public void forEachRemaining(@NotNull Consumer<? super Entry<K, V>> action) {
    final Node<K, V>[] nodes = map.getNodes();
    final int value = getFence();

    if (value < 0) {
      fence = (nodes == null) ? 0 : nodes.length;
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

  public boolean tryAdvance(@NotNull final Consumer<? super Entry<K, V>> action) {
    final Node<K, V>[] table = map.getNodes();
    final int value = getFence();

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

  public int characteristics() {
    return (fence < 0 || estimatedSize == map.getSize() ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
  }

}
