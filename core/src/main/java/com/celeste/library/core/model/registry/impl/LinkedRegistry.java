package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import com.celeste.library.core.registry.type.KeyType;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedRegistry<T, U> extends AbstractRegistry<T, U> {

  public LinkedRegistry() {
    super(new LinkedHashMap<>(), KeyType.STANDARD);
  }

  public LinkedRegistry(final KeyType type) {
    super(new LinkedHashMap<>(), type);
  }

  public LinkedRegistry(final int initialSize) {
    super(new LinkedHashMap<>(initialSize), KeyType.STANDARD);
  }

  public LinkedRegistry(final int initialSize, final KeyType type) {
    super(new LinkedHashMap<>(initialSize), type);
  }

  public LinkedRegistry(final int initialSize, final float density) {
    super(new LinkedHashMap<>(initialSize, density), KeyType.STANDARD);
  }

  public LinkedRegistry(final int initialSize, final float density, final KeyType type) {
    super(new LinkedHashMap<>(initialSize, density), type);
  }

  public LinkedRegistry(final Map<T, U> map) {
    super(new LinkedHashMap<>(map), KeyType.STANDARD);
  }

  public LinkedRegistry(final Map<T, U> map, final KeyType type) {
    super(new LinkedHashMap<>(map), type);
  }

}
