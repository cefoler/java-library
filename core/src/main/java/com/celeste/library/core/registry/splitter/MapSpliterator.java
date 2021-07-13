package com.celeste.library.core.registry.splitter;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.nodes.Node;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapSpliterator<K, V> {

  public final MapRegistry<K, V> map;
  public Node<K, V> current;

  public int index, length, size, modificationsCount;

  final int getTableSize() {
    int current = length;
    if (current > 0) {
      return current;
    }

    this.size = map.getSize();
    this.modificationsCount = map.getModificationsCount();

    final Node<K, V>[] tab = map.getNodes();
    return length = (tab == null)
        ? 0
        : tab.length;
  }

  public final long estimateSize() {
    getTableSize();
    return size;
  }

}
