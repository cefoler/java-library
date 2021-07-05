package com.celeste.library.core.model.registry;

import com.celeste.library.core.model.registry.type.KeyType;
import com.celeste.library.core.model.registry.type.MapType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MapBuilder {

  private MapType mapType;
  private KeyType keyType;

  public static <T, U> Registry<T, U> create(@NotNull final MapType mapType, @NotNull final KeyType keyType) {
    return mapType.get(keyType);
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

  public <T, U> Registry<T, U> build() {
    if (mapType == null) {
      mapType = MapType.MAP;
    }

    if (keyType == null) {
      keyType = KeyType.STANDARD;
    }

    return create(mapType, keyType);
  }

}
