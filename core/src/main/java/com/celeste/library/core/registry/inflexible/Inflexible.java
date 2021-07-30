package com.celeste.library.core.registry.inflexible;

import com.celeste.library.core.registry.collection.Collection;
import com.celeste.library.core.registry.structure.splitter.Spliterators;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;

public interface Inflexible<V> extends Collection<V> {

  int size();

  boolean isEmpty();

  @NotNull Iterator<V> iterator();

  boolean contains(@NotNull Object object);

  boolean containsAll(@NotNull Collection<?> collection);

  boolean add(@NotNull V value);

  boolean addAll(@NotNull Collection<? extends V> collection);

  boolean remove(@NotNull Object value);

  boolean removeAll(@NotNull Collection<?> collection);

  boolean retainAll(@NotNull Collection<?> collection);

  void clear();

  Object[] buildAsArray();

  <T> T[] buildAsArray(T[] object);

  @Override
  default Spliterator<V> spliterator() {
    return Spliterators.spliterator(this, Spliterator.DISTINCT);
  }

}
