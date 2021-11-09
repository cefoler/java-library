package com.celeste.library.core.model.registry.builder;

import com.celeste.library.core.model.registry.Registry;
import com.celeste.library.core.model.registry.type.KeyType;
import com.celeste.library.core.model.registry.type.MapType;
import com.google.common.annotations.Beta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Beta
@Getter
@RequiredArgsConstructor
public final class MapBuilder {

  private MapType mapType;
  private KeyType keyType;

  private int initialCapacity;

  {
    mapType = MapType.MAP;
    keyType = KeyType.STANDARD;
    initialCapacity = 4;
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

  public <T, U> Registry<T, U> build() {
    return mapType.get(this);
  }

}
