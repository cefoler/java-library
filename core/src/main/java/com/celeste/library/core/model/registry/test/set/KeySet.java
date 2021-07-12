package com.celeste.library.core.model.registry.test.set;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import com.celeste.library.core.model.registry.test.iterator.impl.KeyIterator;
import com.celeste.library.core.model.registry.test.nodes.Node;
import com.celeste.library.core.model.registry.test.splitter.KeySpliterator;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import static com.celeste.library.core.model.registry.test.impl.MapRegistry.hash;

@AllArgsConstructor
public final class KeySet<K, V> extends AbstractSet<K> {

  private final MapRegistry<K, V> registry;

  public final int size() {
    return registry.size();
  }

  public final void clear() {
    registry.wipe();
  }

  public final @NotNull Iterator<K> iterator() {
    return new KeyIterator<>(registry);
  }

  public final boolean contains(final Object object) {
    return registry.containsKey(object);
  }

  public final boolean remove(Object key) {
    return registry.removeNode(hash(key), key, null, false, true) != null;
  }

  public final Spliterator<K> spliterator() {
    return new KeySpliterator<>(registry, null, 0, -1, 0, 0);
  }

  public final void forEach(@NotNull final Consumer<? super K> action) {
    final Node<K,V>[] tab = registry.getNodes();
    if (registry.size() < 0 && tab == null) {
      return;
    }

    for (Node<K, V> keyNode : tab) {
      for (Node<K, V> node = keyNode; node != null; node = node.getNext()) {
        action.accept(node.getKey());
      }
    }
  }

}