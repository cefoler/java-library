package com.celeste.library.shared.model.registry.impl;

import com.celeste.library.shared.model.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public final class TreeRegistry<T, U> extends Registry<T, U> {

  /**
   * Creates a new TreeAbstractRegistry
   */
  public TreeRegistry() {
    super(new TreeMap<>());
  }

  /**
   * Creates a new TreeAbstractRegistry with the specific Comparator
   *
   * @param comparator Comparator
   */
  public TreeRegistry(@NotNull final Comparator<? super T> comparator) {
    super(new TreeMap<>(comparator));
  }

  /**
   * Creates a new TreeAbstractRegistry with the specific SortedMap
   *
   * @param sorted SortedMap
   */
  public TreeRegistry(@NotNull final SortedMap<T, ? extends U> sorted) {
    super(new TreeMap<>(sorted));
  }

  /**
   * Creates a new TreeAbstractRegistry from another map
   *
   * @param map Map
   */
  public TreeRegistry(@NotNull final Map<T, U> map) {
    super(new TreeMap<>(map));
  }

}
