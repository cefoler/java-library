package com.celeste.library.core.model.registry.test.splitter;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.nodes.Node;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapSpliterator<K, V> {

  public final MapRegistry<K, V> map;
  public Node<K, V> current;

  public int index, fence, estimatedSize, expectedModificationsCount;

  final int getFence() {
    int current = fence;
    if (current > 0) {
      return current;
    }

    this.estimatedSize = map.getSize();
    this.expectedModificationsCount = map.getModificationsCount();

    final Node<K, V>[] tab = map.getNodes();

    current = fence = (tab == null) ? 0 : tab.length;
    return current;
  }

  public final long estimateSize() {
    getFence();
    return estimatedSize;
  }

}
