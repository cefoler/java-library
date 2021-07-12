package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import com.celeste.library.core.registry.type.KeyType;

import java.util.HashMap;
import java.util.Map;

public class MapRegistry<T, U> extends AbstractRegistry<T, U> {

  public MapRegistry() {
    super(new HashMap<>(), KeyType.STANDARD);
  }

  public MapRegistry(final KeyType type) {
    super(new HashMap<>(), type);
  }

  public MapRegistry(final int initialSize) {
    super(new HashMap<>(initialSize), KeyType.STANDARD);
  }

  public MapRegistry(final int initialSize, final KeyType type) {
    super(new HashMap<>(initialSize), type);
  }

  public MapRegistry(final int initialSize, final float density) {
    super(new HashMap<>(initialSize, density), KeyType.STANDARD);
  }

  public MapRegistry(final int initialSize, final float density, final KeyType type) {
    super(new HashMap<>(initialSize, density), type);
  }

  public MapRegistry(final Map<T, U> map) {
    super(new HashMap<>(map), KeyType.STANDARD);
  }

  public MapRegistry(final Map<T, U> map, final KeyType type) {
    super(new HashMap<>(map), type);
  }

}
