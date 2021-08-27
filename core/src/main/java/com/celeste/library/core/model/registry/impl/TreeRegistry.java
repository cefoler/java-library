package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import com.celeste.library.core.model.registry.type.KeyType;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TreeRegistry<T, U> extends AbstractRegistry<T, U> {

  public TreeRegistry() {
    super(new TreeMap<>(), KeyType.STANDARD);
  }

  public TreeRegistry(final KeyType type) {
    super(new TreeMap<>(), type);
  }

  public TreeRegistry(final Comparator<? super T> comparator) {
    super(new TreeMap<>(comparator), KeyType.STANDARD);
  }

  public TreeRegistry(final Comparator<? super T> comparator, final KeyType type) {
    super(new TreeMap<>(comparator), type);
  }

  public TreeRegistry(final SortedMap<T, ? extends U> sorted, final KeyType type) {
    super(new TreeMap<>(sorted), type);
  }

  public TreeRegistry(final Map<T, U> map, final KeyType type) {
    super(new TreeMap<>(map), type);
  }

}
