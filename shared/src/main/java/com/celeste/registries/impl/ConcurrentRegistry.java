package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ConcurrentRegistry<T, U> extends Registry<T, U> {

  /**
   * Creates ConcurrentRegistry
   */
  public ConcurrentRegistry() {
    super(new ConcurrentHashMap<>());
  }

  /**
   * Creates ConcurrentRegistry with specific size
   *
   * @param initialSize int
   */
  public ConcurrentRegistry(final int initialSize) {
    super(new ConcurrentHashMap<>(initialSize));
  }

  /**
   * Creates ConcurrentRegistry with specific size and density
   *
   * @param initialSize int
   * @param density float
   */
  public ConcurrentRegistry(final int initialSize, final float density) {
    super(new ConcurrentHashMap<>(initialSize, density));
  }

  /**
   * Creates ConcurrentRegistry with specific size, density and threads
   *
   * @param initialSize int
   * @param density float
   * @param threads int
   */
  public ConcurrentRegistry(final int initialSize, final float density, final int threads) {
    super(new ConcurrentHashMap<>(initialSize, density, threads));
  }

  /**
   * Creates ConcurrentRegistry from another map
   *
   * @param map Map<T, U>
   */
  public ConcurrentRegistry(@NotNull final Map<T, U> map) {
    super(new ConcurrentHashMap<>(map));
  }

}
