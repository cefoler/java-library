package com.celeste.library.core.registry.collection;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCollection<E> implements Collection<E> {

  private static final int MAX_ARRAY_SIZE;

  static {
    MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
  }

  public abstract @NotNull Iterator<E> iterator();

  public abstract int size();

  public boolean isEmpty() {
    return size() == 0;
  }

  public boolean contains(@NotNull Object object) {
    for (E value : this) {
      if (object.equals(value)) {
        return true;
      }
    }

    return false;
  }

  public Object[] buildAsArray() {
    final Object[] objects = new Object[size()];

    final Iterator<E> iterator = iterator();
    for (int index = 0; index < objects.length; index++) {
      if (!iterator.hasNext()) {
        return Arrays.copyOf(objects, index);
      }

      objects[index] = iterator.next();
    }

    return iterator.hasNext()
        ? finishToArray(objects, iterator)
        : objects;
  }

  @SuppressWarnings("unchecked")
  public <T> T[] buildAsArray(T[] array) {
    int size = size();
    T[] objectArray = array.length >= size
        ? array
        : (T[]) Array.newInstance(array.getClass().getComponentType(), size);

    Iterator<E> iterator = iterator();
    for (int i = 0; i < objectArray.length; i++) {
      if (iterator.hasNext()) {
        objectArray[i] = (T) iterator.next();
        continue;
      }

      if (array == objectArray) {
        objectArray[i] = null;
        return array;
      }

      if (array.length < i) {
        return Arrays.copyOf(objectArray, i);
      }

      System.arraycopy(objectArray, 0, array, 0, i);
      if (array.length > i) {
        array[i] = null;
      }

      return array;
    }

    return iterator.hasNext()
        ? finishToArray(objectArray, iterator)
        : objectArray;
  }

  @SuppressWarnings("unchecked")
  private static <T> T[] finishToArray(@NotNull T[] array, @NotNull Iterator<?> iterator) {
    int index = array.length;
    while (iterator.hasNext()) {
      int capacity = array.length;
      if (index == capacity) {
        int newCapacity = capacity + (capacity >> 1) + 1;
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
          newCapacity = hugeCapacity(capacity + 1);
        }

        array = Arrays.copyOf(array, newCapacity);
      }

      array[index++] = (T)iterator.next();
    }

    return index == array.length
        ? array
        : Arrays.copyOf(array, index);
  }

  private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) {
      throw new OutOfMemoryError("Required array size too large");
    }

    return minCapacity > MAX_ARRAY_SIZE
        ? Integer.MAX_VALUE
        : MAX_ARRAY_SIZE;
  }

  public boolean add(@NotNull E e) {
    throw new UnsupportedOperationException();
  }

  public boolean remove(@NotNull Object object) {
    final Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      if (object.equals(iterator.next())) {
        iterator.remove();
        return true;
      }
    }

    return false;
  }

  public boolean containsAll(@NotNull Collection<?> collection) {
    for (Object object : collection){
      if (!contains(object)) {
        return false;
      }
    }

    return true;
  }

  public boolean addAll(@NotNull Collection<? extends E> collection) {
    boolean modified = false;
    for (E object : collection){
      if (add(object)) {
        modified = true;
      }
    }

    return modified;
  }

  public boolean removeAll(@NotNull Collection<?> collection) {
    boolean modified = false;

    final Iterator<?> iterator = iterator();
    while (iterator.hasNext()) {
      if (collection.contains(iterator.next())) {
        iterator.remove();
        modified = true;
      }
    }

    return modified;
  }

  public boolean retainAll(@NotNull Collection<?> collection) {
    boolean modified = false;

    final Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      if (!collection.contains(iterator.next())) {
        iterator.remove();
        modified = true;
      }
    }

    return modified;
  }

  public void clear() {
    final Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }
  }

}
