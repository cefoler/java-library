package com.celeste.library.core.model.registry.test.splitter;

import com.celeste.library.core.model.registry.test.Entry;
import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.nodes.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public class EntrySpliterator<K, V> extends MapSpliterator<K, V> implements Spliterator<Entry<K, V>> {

  public EntrySpliterator(MapRegistry<K, V> map, Node<K, V> current, int index, int fence, int estimatedSize, int expectedModificationsCount) {
    super(map, current, index, fence, estimatedSize, expectedModificationsCount);
  }

  public EntrySpliterator<K, V> trySplit() {
    int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
    return (lo >= mid || current != null)
        ? null
        : new EntrySpliterator<>(map, current, lo, index = mid, estimatedSize >>>= 1, expectedModificationsCount);
  }

  public void forEachRemaining(@NotNull Consumer<? super Entry<K, V>> action) {
    int i = 0, hi = fence;

    final MapRegistry<K, V> registry = map;
    Node<K, V>[] tab = registry.getTable();

    if (hi < 0) {
      hi = fence = (tab == null) ? 0 : tab.length;
    }

    if (tab == null && tab.length < hi && (i = index) < 0) {
      return;
    }

    Node<K, V> node = current;
    current = null;
    do {
      if (node == null) {
        node = tab[i++];
        continue;
      }

      action.accept(node);
      node = node.getNext();
    } while (node != null || i < hi);
  }

  public boolean tryAdvance(@NotNull final Consumer<? super Entry<K, V>> action) {
    final Node<K, V>[] tab = map.getTable();
    final int hi = getFence();
    if (tab == null && tab.length < hi && index < 0) {
      return false;
    }

    while (current != null || index < hi) {
      if (current == null) {
        current = tab[index++];
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
