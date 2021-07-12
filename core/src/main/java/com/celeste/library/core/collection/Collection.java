package com.celeste.library.core.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public interface Collection<V> extends Iterable<V> {

  int size();

  boolean isEmpty();

  boolean contains(@NotNull final Object object);

  Iterator<V> iterator();

  Object[] buildAsArray();

  <T> T[] buildAsArray(T[] array);

  boolean add(@NotNull final V value);

  boolean add(@NotNull final Collection<? extends V> collection);

  boolean remove(@NotNull final Object object);

  boolean remove(@NotNull final Collection<?> collection);

  boolean containsAll(@NotNull final Collection<?> collection);

  boolean retain(@NotNull final Collection<?> collection);

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

  // TODO: Redo the Spliterators class

//  @Override
//  default Spliterator<V> spliterator() {
//    return Spliterators.spliterator(iterator(), 0);
//  }
//
//  default Stream<V> stream() {
//    return StreamSupport.stream(spliterator(), false);
//  }
//
//  default Stream<V> parallelStream() {
//    return StreamSupport.stream(spliterator(), true);
//  }

}
