package com.celeste.library.core.model.registry.test.splitter;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.nodes.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;

public class ValueSpliterator<K, V> extends MapSpliterator<K, V> implements Spliterator<V> {

  public ValueSpliterator(MapRegistry<K, V> map, Node<K, V> current, int index, int fence, int estimatedSize, int expectedModificationsCount) {
    super(map, current, index, fence, estimatedSize, expectedModificationsCount);
  }

  public ValueSpliterator<K, V> trySplit() {
    int fence = getFence(), index = this.index, estimatedSize = (index + fence) >>> 1;
    return (index >= estimatedSize || current != null)
        ? null
        : new ValueSpliterator<>(map, current, index, this.index = estimatedSize, this.estimatedSize >>>= 1, expectedModificationsCount);
  }

  public void forEachRemaining(@NotNull Consumer<? super V> action) {
    final Node<K, V>[] tab = map.getNodes();

    int fence = this.fence;
    if (fence < 0) {
      fence = this.fence = (tab == null) ? 0 : tab.length;
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
    final int hi = getFence();

    final Node<K, V>[] tab = map.getNodes();
    if (tab == null || tab.length <= hi && index < 0) {
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
    } while (current != null || index < hi);
  }

  public int characteristics() {
    return (fence < 0 || estimatedSize == map.getSize() ? Spliterator.SIZED : 0);
  }

}
