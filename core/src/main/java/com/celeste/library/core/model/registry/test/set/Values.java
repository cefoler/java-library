package com.celeste.library.core.model.registry.test.set;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.iterator.impl.ValueIterator;
import com.celeste.library.core.model.registry.test.nodes.Node;
import com.celeste.library.core.model.registry.test.splitter.ValueSpliterator;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@AllArgsConstructor
public final class Values<K, V> extends AbstractCollection<V> {

  private final MapRegistry<K, V> registry;

  public final int size() {
    return registry.size();
  }

  public final void clear() {
    registry.wipe();
  }

  public final @NotNull Iterator<V> iterator() {
    return new ValueIterator<>(registry);
  }

  public final boolean contains(final Object object) {
    return registry.containsKey(object);
  }

  public final Spliterator<V> spliterator() {
    return new ValueSpliterator<>(registry, null, 0, -1, 0, 0);
  }

  public final void forEach(@NotNull final Consumer<? super V> action) {
    final Node<K, V>[] tab = registry.getTable();
    if (registry.getSize() < 0 && tab == null) {
      return;
    }

    for (Node<K, V> keyNode : tab) {
      for (Node<K, V> node = keyNode; node != null; node = node.getNext()) {
        action.accept(node.getValue());
      }
    }
  }

}
