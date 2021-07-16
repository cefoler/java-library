package com.celeste.library.core.registry.collection.table;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;

public interface Table<E> extends Collection<E> {

  int size();

  boolean isEmpty();

  boolean contains(@NotNull Object object);

  Iterator<E> iterator();

  Object[] toArray();

  <T> T[] toArray(T @NotNull [] array);

  boolean add(@NotNull E value);

  boolean remove(@NotNull Object object);

  boolean containsAll(@NotNull Collection<?> collection);

  boolean addAll(@NotNull Collection<? extends E> collection);

  boolean addAll(int index, @NotNull Collection<? extends E> collection);

  boolean removeAll(@NotNull Collection<?> collection);

  boolean retainAll(@NotNull Collection<?> collection);

  default void replaceAll(@NotNull UnaryOperator<E> operator) {
    final ListIterator<E> iterator = this.listIterator();
    while (iterator.hasNext()) {
      iterator.set(operator.apply(iterator.next()));
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  default void sort(@NotNull Comparator<? super E> comparator) {
    final Object[] array = toArray();
    Arrays.sort(array, (Comparator) comparator);

    final ListIterator<E> iterator = listIterator();
    for (Object object : array) {
      iterator.next();
      iterator.set((E) object);
    }
  }

  void clear();

  E get(int index);

  E set(int index, E element);

  void add(int index, E element);

  E remove(int index);

  int indexOf(Object object);

  int lastIndexOf(Object object);

  ListIterator<E> listIterator();

  ListIterator<E> listIterator(int index);

  Table<E> subTable(int fromIndex, int toIndex);

  @Override
  default Spliterator<E> spliterator() {
    return Spliterators.spliterator(this, Spliterator.ORDERED);
  }

}
