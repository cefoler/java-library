package com.celeste.library.core.registry.structure.splitter.impl;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.structure.nodes.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public final class ValueSplitter<K, V> extends MapSplitter<K, V>
    implements Spliterator<V> {

  public ValueSplitter(MapRegistry<K, V> map, Node<K, V> current, int index, int fence, int estimatedSize, int expectedModificationsCount) {
    super(map, current, index, fence, estimatedSize, expectedModificationsCount);
  }

  public ValueSplitter<K, V> trySplit() {
    int fence = getTableSize(), index = this.index, estimatedSize = (index + fence) >>> 1;
    return (index >= estimatedSize || current != null)
        ? new ValueSplitter<>(map, current, index, this.index = estimatedSize, this.size >>>= 1, modificationsCount)
        : null;
  }

  public void forEachRemaining(@NotNull Consumer<? super V> action) {
    final Node<K, V>[] tab = map.getNodes();

    int fence = this.length;
    if (fence < 0) {
      fence = this.length = (tab == null) ? 0 : tab.length;
    }

    int i = index;
    if (tab == null) {
      return;
    }

    if (tab.length >= fence && i >= 0 && i < (index = fence) || current != null) {
      Node<K, V> node = current;
      current = null;
      do {
        if (node == null) {
          node = tab[i++];
        }

        action.accept(node.getValue());
        node = node.getNext();
      } while (node != null || i < fence);
    }
  }

  public boolean tryAdvance(@NotNull final Consumer<? super V> action) {
    final int size = getTableSize();

    final Node<K, V>[] tab = map.getNodes();
    if (tab == null || tab.length <= size && index < 0) {
      return false;
    }

    do {
      if (current == null) {
        current = tab[index++];
      }

      final V value = current.getValue();
      current = current.getNext();

      action.accept(value);
      return true;
    } while (current != null || index < size);
  }

  public int characteristics() {
    return (length < 0 || size == map.getSize() ? Spliterator.SIZED : 0);
  }

}
