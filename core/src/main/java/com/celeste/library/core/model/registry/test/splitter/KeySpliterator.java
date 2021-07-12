package com.celeste.library.core.model.registry.test.splitter;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.nodes.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public final class KeySpliterator<K, V> extends MapSpliterator<K, V> implements Spliterator<K> {

  public KeySpliterator(MapRegistry<K, V> map, Node<K, V> current, int index, int fence, int estimatedSize, int expectedModificationsCount) {
    super(map, current, index, fence, estimatedSize, expectedModificationsCount);
  }

  public KeySpliterator<K, V> trySplit() {
    int fence = getFence(), index = this.index, estimatedSize = (index + fence) >>> 1;
    return (index >= estimatedSize || current != null)
        ? new KeySpliterator<>(map, current, index, this.index = estimatedSize, this.estimatedSize >>>= 1, expectedModificationsCount)
        : null;
  }

  public void forEachRemaining(@NotNull final Consumer<? super K> action) {
    int index = 0;
    final Node<K, V>[] nodes = map.getNodes();

    if (fence < 0) {
      fence = (nodes == null) ? 0 : nodes.length;
    }

    if (nodes == null || nodes.length < fence && (index = this.index) < 0 || current == null) {
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
    } while (node != null || index < fence);
  }

  public boolean tryAdvance(@NotNull Consumer<? super K> action) {
    final int value = getFence();

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

  public int characteristics() {
    return (fence < 0 || estimatedSize == map.getSize() ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
  }

}