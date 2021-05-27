package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakRegistry<T, U> extends AbstractRegistry<T, U> {

  /**
   * Creates a new WeakAbstractRegistry
   */
  public WeakRegistry() {
    super(new WeakHashMap<>());
  }

  /**
   * Creates a new WeakAbstractRegistry with specific size
   *
   * @param initialSize int
   */
  public WeakRegistry(final int initialSize) {
    super(new WeakHashMap<>(initialSize));
  }

  /**
   * Creates a new WeakAbstractRegistry with specific size and density
   *
   * @param initialSize int
   * @param density float
   */
  public WeakRegistry(final int initialSize, final float density) {
    super(new WeakHashMap<>(initialSize, density));
  }

  /**
   * Creates a new WeakAbstractRegistry from another map
   *
   * @param map Map
   */
  public WeakRegistry(final Map<T, U> map) {
    super(new WeakHashMap<>(map));
  }
}
