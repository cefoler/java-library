package com.celeste.library.core.model.registry;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public interface Registry<T, U> extends Serializable, Cloneable {

  U register(final T key, final U value);

  U registerIfAbsent(final T key, final U value);

  U compute(final T key, final BiFunction<T, U, U> function);

  U computeIfAbsent(final T key, final Function<T, U> function);

  U remove(final T key);

  U replace(final T key, final U value);

  @Nullable
  U get(final T key);

  boolean contains(final T key);

  Set<Entry<T, U>> getEntrySet();

  Set<T> getKeys();

  Collection<U> getAll();

  List<U> sort(final Comparator<U> comparator);

  int size();

  boolean isEmpty();

  Registry<T, U> wipe();

  Map<T, U> getMap();

  Registry<T, U> forEach(final BiConsumer<? super T, ? super U> action);

}
