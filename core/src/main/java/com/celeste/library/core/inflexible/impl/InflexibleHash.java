package com.celeste.library.core.inflexible.impl;

import com.celeste.library.core.inflexible.Inflexible;
import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.splitter.KeySpliterator;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

public final class InflexibleHash<V> extends AbstractSet<V>
    implements Inflexible<V>, Cloneable, Serializable {

  private final transient MapRegistry<V, Object> registry;

  private static final Object DEFAULT_VALUE;

  static {
    DEFAULT_VALUE = new Object();
  }

  public InflexibleHash() {
    this(MapRegistry::new);
  }

  public InflexibleHash(Supplier<? extends MapRegistry<V, Object>> supplier) {
    this.registry = supplier.get();
  }

  public InflexibleHash(final int initialCapacity) {
    registry = new MapRegistry<>(initialCapacity);
  }

  public InflexibleHash(@NotNull final Collection<? extends V> collection) {
    this.registry = new MapRegistry<>();
    addAll(collection);
  }

  public InflexibleHash(final int initialCapacity, final float loadFactor) {
    this.registry = new MapRegistry<>(initialCapacity, loadFactor);
  }

  @Override
  public @NotNull Iterator<V> iterator() {
    return registry.getKeys().iterator();
  }

  @Override
  public int size() {
    return registry.size();
  }

  @Override
  public boolean isEmpty() {
    return registry.isEmpty();
  }

  @Override
  public boolean contains(@NotNull final Object object) {
    return registry.containsKey(object);
  }

  @Override
  public boolean add(@NotNull final V value) {
    return registry.register(value, DEFAULT_VALUE) == null;
  }

  @Override
  public boolean remove(@NotNull final Object object) {
    return registry.remove(object) == DEFAULT_VALUE;
  }

  @Override
  public void clear() {
    registry.wipe();
  }

  public Spliterator<V> spliterator() {
    return new KeySpliterator<>(registry, null, 0, -1, 0, 0);
  }

}
