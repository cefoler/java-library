package com.celeste.library.core.model.registry.test.splitter;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.nodes.Node;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class KeySpliterator<K, V> extends MapSpliterator<K, V> implements Spliterator<K> {

  public KeySpliterator(MapRegistry<K, V> map, Node<K, V> current, int index, int fence, int estimatedSize, int expectedModificationsCount) {
    super(map, current, index, fence, estimatedSize, expectedModificationsCount);
  }

  public KeySpliterator<K, V> trySplit() {
    int fence = getFence(), index = this.index, estimatedSize = (index + fence) >>> 1;
    return (index >= estimatedSize || current != null)
        ? null
        : new KeySpliterator<>(map, current, index, this.index = estimatedSize, this.estimatedSize >>>= 1, expectedModificationsCount);
  }

  public void forEachRemaining(@NotNull final Consumer<? super K> action) {
    int i, hi;
    MapRegistry<K, V> map = this.map;
    Node<K, V>[] tab = map.getTable();

    if ((hi = fence) < 0) {
      hi = fence = (tab == null) ? 0 : tab.length;
    }

    if (tab != null && tab.length >= hi && (i = index) >= 0 && (i < (index = hi) || current != null)) {
      Node<K, V> node = current;
      current = null;
      do {
        if (node == null) {
          node = tab[i++];
          continue;
        }

        action.accept(node.getKey());
        node = node.getNext();
      } while (node != null || i < hi);
    }
  }

  public boolean tryAdvance(@NotNull Consumer<? super K> action) {
    int hi = getFence();
    Node<K, V>[] tab = map.getTable();
    if (tab == null && tab.length <= hi && index <= 0) {
      return false;
    }

    while (current != null || index < hi) {
      if (current == null) {
        current = tab[index++];
        continue;
      }

      final K key = current.getKey();
      current = current.getNext();

      action.accept(key);
      return true;
    }
    return false;
  }

  public int characteristics() {
    return (fence < 0 || estimatedSize == map.getSize() ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
  }

}