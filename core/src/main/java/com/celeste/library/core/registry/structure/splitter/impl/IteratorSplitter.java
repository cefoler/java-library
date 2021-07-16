package com.celeste.library.core.registry.structure.splitter.impl;

import com.celeste.library.core.registry.collection.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public final class IteratorSplitter<T> implements Spliterator<T> {

  private static final int BATCH_UNIT;
  private static final int MAX_BATCH;

  static {
    BATCH_UNIT = 1 << 10;
    MAX_BATCH = 1 << 25;
  }

  private final Collection<? extends T> collection;
  private Iterator<? extends T> iterator;

  private final int characteristics;
  private long estimatedSize;
  private int batch;

  public IteratorSplitter(final Collection<? extends T> collection, int characteristics) {
    this.collection = collection;
    this.iterator = null;
    this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
        ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
        : characteristics;
  }

  public IteratorSplitter(final Iterator<? extends T> iterator, long size, int characteristics) {
    this.collection = null;
    this.iterator = iterator;
    this.estimatedSize = size;
    this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
        ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
        : characteristics;
  }

  public IteratorSplitter(final Iterator<? extends T> iterator, int characteristics) {
    this.collection = null;
    this.iterator = iterator;
    this.estimatedSize = Long.MAX_VALUE;
    this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
  }

  @Override
  public Spliterator<T> trySplit() {
    if (iterator == null) {
      this.iterator = collection.iterator();
      this.estimatedSize = collection.size();
    }

    if (estimatedSize > 1 && iterator.hasNext()) {
      int batchValue = batch + BATCH_UNIT;
      if (batchValue > estimatedSize) {
        batchValue = (int) estimatedSize;
      }

      if (batchValue > MAX_BATCH) {
        batchValue = MAX_BATCH;
      }

      int index = 0;

      Object[] objects = new Object[batchValue];
      do {
        objects[index] = iterator.next();
      } while (index++ < batchValue && iterator.hasNext());

      batch = index;
      if (estimatedSize != Long.MAX_VALUE) {
        estimatedSize -= index;
      }

      return new ArraySplitter<>(objects, 0, index, characteristics);
    }

    return null;
  }

  @Override
  public void forEachRemaining(@NotNull final  Consumer<? super T> action) {
    if (iterator == null) {
      this.iterator = collection.iterator();
      this.estimatedSize = collection.size();
    }

    iterator.forEachRemaining(action);
  }

  @Override
  public boolean tryAdvance(@NotNull final Consumer<? super T> action) {
    if (iterator == null) {
      this.iterator = collection.iterator();
      this.estimatedSize = collection.size();
    }

    if (iterator.hasNext()) {
      action.accept(iterator.next());
      return true;
    }

    return false;
  }

  @Override
  public long estimateSize() {
    if (iterator == null) {
      this.iterator = collection.iterator();
      return estimatedSize = collection.size();
    }

    return estimatedSize;
  }

  @Override
  public int characteristics() { return characteristics; }

  @Override
  public Comparator<? super T> getComparator() {
    return null;
  }

}
