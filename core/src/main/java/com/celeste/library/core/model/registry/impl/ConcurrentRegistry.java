package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ConcurrentRegistry<T, U> extends AbstractRegistry<T, U> {

  /**
   * Creates ConcurrentAbstractRegistry
   */
  public ConcurrentRegistry() {
    super(new ConcurrentHashMap<>());
  }

  /**
   * Creates ConcurrentAbstractRegistry with specific size
   *
   * @param initialSize int
   */
  public ConcurrentRegistry(final int initialSize) {
    super(new ConcurrentHashMap<>(initialSize));
  }

  /**
   * Creates ConcurrentAbstractRegistry with specific size and density
   *
   * @param initialSize int
   * @param density float
   */
  public ConcurrentRegistry(final int initialSize, final float density) {
    super(new ConcurrentHashMap<>(initialSize, density));
  }

  /**
   * Creates ConcurrentAbstractRegistry with specific size, density and threads
   *
   * @param initialSize int
   * @param density float
   * @param threads int
   */
  public ConcurrentRegistry(final int initialSize, final float density, final int threads) {
    super(new ConcurrentHashMap<>(initialSize, density, threads));
  }

  /**
   * Creates ConcurrentAbstractRegistry from another map
   *
   * @param map Map<T, U>
   */
  public ConcurrentRegistry(final Map<T, U> map) {
    super(new ConcurrentHashMap<>(map));
  }

}
