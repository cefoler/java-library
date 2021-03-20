package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class MapRegistry<T, U> extends Registry<T, U> {

  /**
   * Creates a new MapRegistry
   */
  public MapRegistry() {
    super(new HashMap<>());
  }

  /**
   * Creates a new MapRegistry with specific size
   *
   * @param initialSize int
   */
  public MapRegistry(final int initialSize) {
    super(new HashMap<>(initialSize));
  }

  /**
   * Creates a new MapRegistry with specific size and density
   *
   * @param initialSize int
   * @param density float
   */
  public MapRegistry(final int initialSize, final float density) {
    super(new HashMap<>(initialSize, density));
  }

  /**
   * Creates a new MapRegistry from another map
   *
   * @param map Map<T, U>
   */
  public MapRegistry(@NotNull final Map<T, U> map) {
    super(new HashMap<>(map));
  }

}
