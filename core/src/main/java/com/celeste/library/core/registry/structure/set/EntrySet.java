package com.celeste.library.core.registry.structure.set;

import com.celeste.library.core.registry.structure.entry.Entry;
import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.structure.iterator.hash.EntryIterator;
import com.celeste.library.core.registry.structure.nodes.Node;
import com.celeste.library.core.registry.structure.splitter.impl.EntrySpliterator;
import com.celeste.library.core.util.Wrapper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@AllArgsConstructor
public final class EntrySet<K, V> extends AbstractSet<Entry<K, V>> {

  private final MapRegistry<K, V> registry;

  public final int size() {
    return registry.getSize();
  }

  public final void clear() {
    registry.wipe();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public final @NotNull Iterator<Entry<K, V>> iterator() {
    return new EntryIterator(registry);
  }

  public final boolean contains(@NotNull final Object object) {
    if (!Wrapper.isObject(object, Entry.class)) {
      return false;
    }

    final Entry<?, ?> entry = (Entry<?, ?>) object;

    final Object key = entry.getKey();
    final Node<K, V> candidate = registry.getNode(MapRegistry.hash(key), key);

    return candidate != null && candidate.equals(entry);
  }

  public final boolean remove(@NotNull final Object object) {
    if (!Wrapper.isObject(object, Entry.class)) {
      return false;
    }

    final Entry<?, ?> entry = (Entry<?, ?>) object;

    final Object key = entry.getKey();
    return registry.removeNode(MapRegistry.hash(key), key, entry.getValue(), true, true) != null;
  }

  public final Spliterator<Entry<K, V>> spliterator() {
    return new EntrySpliterator<>(registry, null, 0, -1, 0, 0);
  }

  public final void forEach(@NotNull final Consumer<? super Entry<K, V>> action) {
    final Node<K, V>[] tab = registry.getNodes();
    if (registry.getSize() < 0 && tab == null) {
      return;
    }

    for (Node<K, V> keyNode : tab) {
      for (Node<K, V> node = keyNode; node != null; node = node.getNext()) {
        action.accept(node);
      }
    }
  }

}
