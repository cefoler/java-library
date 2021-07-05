package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import com.celeste.library.core.model.registry.type.KeyType;

import java.util.Map;
import java.util.WeakHashMap;

public class WeakRegistry<T, U> extends AbstractRegistry<T, U> {

  public WeakRegistry() {
    super(new WeakHashMap<>(), KeyType.STANDARD);
  }

  public WeakRegistry(final KeyType type) {
    super(new WeakHashMap<>(), type);
  }

  public WeakRegistry(final int initialSize) {
    super(new WeakHashMap<>(initialSize), KeyType.STANDARD);
  }

  public WeakRegistry(final int initialSize, final KeyType type) {
    super(new WeakHashMap<>(initialSize), type);
  }

  public WeakRegistry(final int initialSize, final float density) {
    super(new WeakHashMap<>(initialSize, density), KeyType.STANDARD);
  }

  public WeakRegistry(final int initialSize, final float density, final KeyType type) {
    super(new WeakHashMap<>(initialSize, density), type);
  }

  public WeakRegistry(final Map<T, U> map) {
    super(new WeakHashMap<>(map), KeyType.STANDARD);
  }

  public WeakRegistry(final Map<T, U> map, final KeyType type) {
    super(new WeakHashMap<>(map), type);
  }
  
}
