package com.celeste.library.core.registry.structure.set;

import com.celeste.library.core.registry.Registry;
import com.celeste.library.core.registry.impl.LinkedRegistry;
import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.structure.iterator.hash.ValueIterator;
import com.celeste.library.core.registry.structure.iterator.linked.LinkedValueIterator;
import com.celeste.library.core.registry.structure.nodes.Node;
import com.celeste.library.core.registry.structure.nodes.impl.LinkedNode;
import com.celeste.library.core.registry.structure.splitter.impl.ValueSplitter;
import com.celeste.library.core.util.Wrapper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

@AllArgsConstructor
public final class Values<K, V> extends AbstractCollection<V> {

  private final Registry<K, V> registry;

  public int size() {
    return registry.size();
  }

  public void clear() {
    registry.wipe();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public @NotNull Iterator<V> iterator() {
    if (Wrapper.isObject(registry, LinkedRegistry.class)) {
      return new LinkedValueIterator((LinkedRegistry<K, V>) registry);
    }

    return new ValueIterator<>((MapRegistry<K, V>) registry);
  }

  public boolean contains(final Object object) {
    return registry.containsKey(object);
  }

  public Spliterator<V> spliterator() {
    if (Wrapper.isObject(registry, MapRegistry.class)) {
      return new ValueSplitter<>((MapRegistry<K, V>) registry, null, 0, -1, 0, 0);
    }

    if (Wrapper.isObject(registry, LinkedRegistry.class)) {
      return Spliterators.spliterator(this, Spliterator.SIZED | Spliterator.ORDERED);
    }

    return null;
  }

  public void forEach(@NotNull final Consumer<? super V> action) {
    if (Wrapper.isObject(registry, MapRegistry.class)) {
      final Node<K, V>[] tab = ((MapRegistry<K, V>) registry).getNodes();
      if (registry.size() < 0 && tab == null) {
        return;
      }

      for (Node<K, V> keyNode : tab) {
        for (Node<K, V> node = keyNode; node != null; node = node.getNext()) {
          action.accept(node.getValue());
        }
      }
    }

    if (Wrapper.isObject(registry, LinkedRegistry.class)) {
      for (LinkedNode<K,V> node = ((LinkedRegistry<K, V>) registry).getEldest(); node != null; node = node.getAfter()) {
        action.accept(node.getValue());
      }
    }
  }

}
