package com.celeste.library.core.registry.structure.set;

import com.celeste.library.core.registry.Registry;
import com.celeste.library.core.registry.impl.LinkedRegistry;
import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.structure.iterator.hash.KeyIterator;
import com.celeste.library.core.registry.structure.iterator.linked.LinkedKeyIterator;
import com.celeste.library.core.registry.structure.nodes.Node;
import com.celeste.library.core.registry.structure.nodes.impl.LinkedNode;
import com.celeste.library.core.registry.structure.splitter.impl.KeySplitter;
import com.celeste.library.core.util.Wrapper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

import static com.celeste.library.core.registry.impl.MapRegistry.hash;

@AllArgsConstructor
public final class KeySet<K, V> extends AbstractSet<K> {

  private final Registry<K, V> registry;

  public int size() {
    return registry.size();
  }

  public void clear() {
    registry.wipe();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Iterator<K> iterator() {
    if (Wrapper.isObject(registry, LinkedRegistry.class)) {
      return new LinkedKeyIterator((LinkedRegistry) registry);
    }

    if (Wrapper.isObject(registry, MapRegistry.class)) {
      return new KeyIterator<>((LinkedRegistry) registry);
    }

    return null;
  }

  public boolean contains(@NotNull final Object object) {
    return registry.containsKey(object);
  }

  public boolean remove(Object key) {
    if (Wrapper.isObject(registry, LinkedRegistry.class)) {
      return ((MapRegistry<K, V>) registry).removeNode(hash(key), key, null, false, true) != null;
    }

    if (Wrapper.isObject(registry, MapRegistry.class)) {
      return ((LinkedRegistry<K, V>) registry).removeNode(hash(key), key, null, false, true) != null;
    }

    return false;
  }

  @SuppressWarnings({"rawuse"})
  public Spliterator<K> spliterator()  {
    if (Wrapper.isObject(registry, LinkedRegistry.class)) {
      return Spliterators.spliterator(this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT);
    }

    if (Wrapper.isObject(registry, MapRegistry.class)) {
      return new KeySplitter<>((MapRegistry<K, V>) registry, null, 0, -1, 0, 0);
    }

    return null;
  }

  public void forEach(@NotNull final Consumer<? super K> action) {
    if (Wrapper.isObject(registry, LinkedRegistry.class)) {
      for (LinkedNode<K,V> node = ((LinkedRegistry<K, V>) registry).getEldest(); node != null; node = node.getAfter()) {
        action.accept(node.getKey());
      }
    }

    if (Wrapper.isObject(registry, MapRegistry.class)) {
      final Node<K,V>[] tab = ((MapRegistry<K, V>) registry).getNodes();
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

}
