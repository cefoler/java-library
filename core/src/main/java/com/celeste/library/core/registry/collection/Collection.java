package com.celeste.library.core.registry.collection;

import com.celeste.library.core.registry.structure.splitter.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public interface Collection<V> extends Iterable<V> {

  int size();

  boolean isEmpty();

  boolean contains(@NotNull final Object object);

  @NotNull Iterator<V> iterator();

  Object[] buildAsArray();

  <T> T[] buildAsArray(T[] array);

  boolean add(@NotNull final V value);

  boolean addAll(@NotNull final Collection<? extends V> collection);

  boolean remove(@NotNull final Object object);

  boolean removeAll(@NotNull final Collection<?> collection);

  boolean containsAll(@NotNull final Collection<?> collection);

  boolean retainAll(@NotNull final Collection<?> collection);

  void clear();

  default boolean removeIf(@NotNull final Predicate<? super V> filter) {
    boolean removed = false;

    final Iterator<V> each = iterator();
    while (each.hasNext()) {
      if (!filter.test(each.next())) {
        continue;
      }

      each.remove();
      removed = true;
    }

    return removed;
  }

  @Override
  default Spliterator<V> spliterator() {
    return Spliterators.spliterator(this, 0);
  }

  default Stream<V> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  default Stream<V> parallelStream() {
    return StreamSupport.stream(spliterator(), true);
  }

}
