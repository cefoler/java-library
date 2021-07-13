package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import com.celeste.library.core.registry.type.KeyType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentRegistry<T, U> extends AbstractRegistry<T, U> {

  public ConcurrentRegistry() {
    super(new ConcurrentHashMap<>(), KeyType.STANDARD);
  }

  public ConcurrentRegistry(final KeyType type) {
    super(new ConcurrentHashMap<>(), type);
  }

  public ConcurrentRegistry(final int initialSize) {
    super(new ConcurrentHashMap<>(initialSize), KeyType.STANDARD);
  }

  public ConcurrentRegistry(final int initialSize, final KeyType type) {
    super(new ConcurrentHashMap<>(initialSize), type);
  }

  public ConcurrentRegistry(final int initialSize, final float density) {
    super(new ConcurrentHashMap<>(initialSize, density), KeyType.STANDARD);
  }

  public ConcurrentRegistry(final int initialSize, final float density, final KeyType type) {
    super(new ConcurrentHashMap<>(initialSize, density), type);
  }

  public ConcurrentRegistry(final int initialSize, final float density, final int threads) {
    super(new ConcurrentHashMap<>(initialSize, density, threads), KeyType.STANDARD);
  }

  public ConcurrentRegistry(final int initialSize, final float density, final int threads, final KeyType type) {
    super(new ConcurrentHashMap<>(initialSize, density, threads), type);
  }

  public ConcurrentRegistry(final Map<T, U> map) {
    super(new ConcurrentHashMap<>(map), KeyType.STANDARD);
  }

  public ConcurrentRegistry(final Map<T, U> map, final KeyType type) {
    super(new ConcurrentHashMap<>(map), type);
  }

}
