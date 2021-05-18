package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LinkedRegistry<T, U> extends AbstractRegistry<T, U> {

  /**
   * Creates a LinkedAbstractRegistry
   */
  public LinkedRegistry() {
    super(new LinkedHashMap<>());
  }

  /**
   * Creates a LinkedAbstractRegistry with specific size
   *
   * @param initialSize int
   */
  public LinkedRegistry(final int initialSize) {
    super(new ConcurrentHashMap<>(initialSize));
  }

  /**
   * Creates a LinkedAbstractRegistry with specific size and density
   *
   * @param initialSize int
   * @param density float
   */
  public LinkedRegistry(final int initialSize, final float density) {
    super(new LinkedHashMap<>(initialSize, density));
  }

  /**
   * Creates a LinkedAbstractRegistry with specific size, density and access order
   *
   * @param initialSize int
   * @param density float
   * @param accessOrder boolean
   */
  public LinkedRegistry(final int initialSize, final float density, final boolean accessOrder) {
    super(new LinkedHashMap<>(initialSize, density, accessOrder));
  }

  /**
   * Creates a LinkedAbstractRegistry from a map
   *
   * @param map Map
   */
  public LinkedRegistry(final Map<T, U> map) {
    super(new LinkedHashMap<>(map));
  }

}
