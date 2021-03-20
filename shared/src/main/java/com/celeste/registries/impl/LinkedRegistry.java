package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LinkedRegistry<T, U> extends Registry<T, U> {

  /**
   * Creates a LinkedRegistry
   */
  public LinkedRegistry() {
    super(new LinkedHashMap<>());
  }

  /**
   * Creates a LinkedRegistry with specific size
   *
   * @param initialSize int
   */
  public LinkedRegistry(final int initialSize) {
    super(new ConcurrentHashMap<>(initialSize));
  }

  /**
   * Creates a LinkedRegistry with specific size and density
   *
   * @param initialSize int
   * @param density float
   */
  public LinkedRegistry(final int initialSize, final float density) {
    super(new LinkedHashMap<>(initialSize, density));
  }

  /**
   * Creates a LinkedRegistry with specific size, density and access order
   *
   * @param initialSize int
   * @param density float
   * @param accessOrder boolean
   */
  public LinkedRegistry(final int initialSize, final float density, final boolean accessOrder) {
    super(new LinkedHashMap<>(initialSize, density, accessOrder));
  }

  /**
   * Creates a LinkedRegistry from a map
   *
   * @param map Map<T, U>
   */
  public LinkedRegistry(@NotNull final Map<T, U> map) {
    super(new LinkedHashMap<>(map));
  }

}
