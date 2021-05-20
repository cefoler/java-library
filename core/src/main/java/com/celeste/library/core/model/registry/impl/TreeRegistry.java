package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public final class TreeRegistry<T, U> extends AbstractRegistry<T, U> {

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
  public TreeRegistry(final Comparator<? super T> comparator) {
    super(new TreeMap<>(comparator));
  }

  /**
   * Creates a new TreeAbstractRegistry with the specific SortedMap
   *
   * @param sorted SortedMap
   */
  public TreeRegistry(final SortedMap<T, ? extends U> sorted) {
    super(new TreeMap<>(sorted));
  }

  /**
   * Creates a new TreeAbstractRegistry from another map
   *
   * @param map Map
   */
  public TreeRegistry(final Map<T, U> map) {
    super(new TreeMap<>(map));
  }

}
