package com.celeste.library.core.inflexible.impl;

import com.celeste.library.core.inflexible.Inflexible;
import com.celeste.library.core.inflexible.type.InflexibleType;
import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.splitter.KeySpliterator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor
public final class InflexibleList<V> extends AbstractSet<V>
    implements Inflexible<V>, Cloneable, Serializable {

  private static final Object DEFAULT_VALUE;

  static {
    DEFAULT_VALUE = new Object();
  }

  private final transient MapRegistry<V, Object> registry;
  private final InflexibleType type;

  public InflexibleList() {
    this(InflexibleType.ARRAY, MapRegistry::new);
  }

  public InflexibleList(final InflexibleType type) {
    this(type, MapRegistry::new);
  }

  public InflexibleList(final Supplier<? extends MapRegistry<V, Object>> supplier) {
    this(InflexibleType.ARRAY, supplier);
  }

  public InflexibleList(final InflexibleType type, final Supplier<? extends MapRegistry<V, Object>> supplier) {
    this.registry = supplier.get();
    this.type = type;
  }

  public InflexibleList(final int initialCapacity) {
    this(new MapRegistry<>(initialCapacity), InflexibleType.ARRAY);
  }

  public InflexibleList(final int initialCapacity, final InflexibleType type) {
    this(new MapRegistry<>(initialCapacity), type);
  }

  public InflexibleList(@NotNull final Collection<? extends V> collection) {
    this(MapRegistry::new);
    addAll(collection);
  }

  public InflexibleList(@NotNull final Collection<? extends V> collection, final InflexibleType type) {
    this(new MapRegistry<>(), type);
    addAll(collection);
  }

  public InflexibleList(final int initialCapacity, final float loadFactor) {
    this(new MapRegistry<>(initialCapacity, loadFactor), InflexibleType.ARRAY);
  }

  public InflexibleList(final int initialCapacity, final float loadFactor, final InflexibleType type) {
    this(new MapRegistry<>(initialCapacity, loadFactor), type);
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
    switch(type) {
      case ARRAY: return new KeySpliterator<>(registry, null, 0, -1, 0, 0);
      case LINKED: return Spliterators.spliterator(this, Spliterator.DISTINCT | Spliterator.ORDERED);
      default: throw new UnsupportedOperationException("The type provided doesn't have a Spliterator.");
    }
  }

}
