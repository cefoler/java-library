package com.celeste.library.core.registry.structure.splitter.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public final class ArraySplitter<T> implements Spliterator<T> {

  private final Object[] array;
  private int index;
  private final int fence;
  private final int characteristics;

  public ArraySplitter(final Object[] array, final int additionalCharacteristics) {
    this(array, 0, array.length, additionalCharacteristics);
  }

  public ArraySplitter(final Object[] array, final int origin, final int fence, final int additionalCharacteristics) {
    this.array = array;
    this.index = origin;
    this.fence = fence;
    this.characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
  }

  @Override
  public Spliterator<T> trySplit() {
    int lo = index, mid = (lo + fence) >>> 1;
    return (lo >= mid)
        ? null
        : new ArraySplitter<>(array, lo, index = mid, characteristics);
  }

  @Override @SuppressWarnings("unchecked")
  public void forEachRemaining(@NotNull final Consumer<? super T> action) {
    int index = this.index;
    if (array.length >= fence && index >= 0 && index < (this.index = fence)) {
      do {
        action.accept((T) array[index]);
      } while (index++ < fence);
    }
  }

  @Override @SuppressWarnings("unchecked")
  public boolean tryAdvance(@NotNull final Consumer<? super T> action) {
    if (index >= 0 && index < fence) {
      action.accept((T) array[index++]);
      return true;
    }

    return false;
  }

  @Override
  public long estimateSize() {
    return fence - index;
  }

  @Override
  public int characteristics() {
    return characteristics;
  }

  @Override
  public Comparator<? super T> getComparator() {
    return null;
  }

}
