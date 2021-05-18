package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import java.util.HashMap;
import java.util.Map;

public final class MapRegistry<T, U> extends AbstractRegistry<T, U> {

  /**
   * Creates a new MapAbstractRegistry
   */
  public MapRegistry() {
    super(new HashMap<>());
  }

  /**
   * Creates a new MapAbstractRegistry with specific size
   *
   * @param initialSize int
   */
  public MapRegistry(final int initialSize) {
    super(new HashMap<>(initialSize));
  }

  /**
   * Creates a new MapAbstractRegistry with specific size and density
   *
   * @param initialSize int
   * @param density float
   */
  public MapRegistry(final int initialSize, final float density) {
    super(new HashMap<>(initialSize, density));
  }

  /**
   * Creates a new MapAbstractRegistry from another map
   *
   * @param map Map
   */
  public MapRegistry(final Map<T, U> map) {
    super(new HashMap<>(map));
  }

}
