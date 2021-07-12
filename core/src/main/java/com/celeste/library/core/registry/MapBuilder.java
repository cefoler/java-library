package com.celeste.library.core.registry;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.type.KeyType;
import com.celeste.library.core.registry.type.MapType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

@Getter
@RequiredArgsConstructor
public final class MapBuilder {

  private MapType mapType;
  private KeyType keyType;

  private int initialCapacity;
  private float loadFactor;
  private boolean accessOrder;

  {
    mapType = MapType.MAP;
    keyType = KeyType.STANDARD;
    initialCapacity = MapRegistry.DEFAULT_INITIAL_CAPACITY;
    loadFactor = MapRegistry.DEFAULT_LOAD_FACTOR;
    accessOrder = false;
  }

  public static <T, U> Registry<T, U> create(@NotNull final MapType mapType, @NotNull final KeyType keyType) {
    return MapBuilder.create()
        .type(mapType)
        .key(keyType)
        .build();
  }

  public static MapBuilder create() {
    return new MapBuilder();
  }

  public MapBuilder type(final MapType mapType) {
    this.mapType = mapType;
    return this;
  }

  public MapBuilder key(final KeyType keyType) {
    this.keyType = keyType;
    return this;
  }

  public MapBuilder capacity(final int initialCapacity) {
    this.initialCapacity = initialCapacity;
    return this;
  }

  public MapBuilder loadFactor(final float loadFactor) {
    this.loadFactor = loadFactor;
    return this;
  }

  public MapBuilder accessOrder(final boolean accessOrder) {
    this.accessOrder = accessOrder;
    return this;
  }

  public <T, U> Registry<T, U> build() {
    return mapType.get(this);
  }

}
