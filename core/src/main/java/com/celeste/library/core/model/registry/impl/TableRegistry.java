package com.celeste.library.core.model.registry.impl;

import com.celeste.library.core.model.registry.AbstractRegistry;
import com.celeste.library.core.model.registry.type.KeyType;

import java.util.Hashtable;
import java.util.Map;

public class TableRegistry<T, U> extends AbstractRegistry<T, U> {

  public TableRegistry() {
    super(new Hashtable<>(), KeyType.STANDARD);
  }

  public TableRegistry(final KeyType type) {
    super(new Hashtable<>(), type);
  }

  public TableRegistry(final int initialSize) {
    super(new Hashtable<>(initialSize), KeyType.STANDARD);
  }

  public TableRegistry(final int initialSize, final KeyType type) {
    super(new Hashtable<>(initialSize), type);
  }

  public TableRegistry(final int initialSize, final float density) {
    super(new Hashtable<>(initialSize, density), KeyType.STANDARD);
  }

  public TableRegistry(final int initialSize, final float density, final KeyType type) {
    super(new Hashtable<>(initialSize, density), type);
  }

  public TableRegistry(final Map<T, U> map) {
    super(new Hashtable<>(map), KeyType.STANDARD);
  }

  public TableRegistry(final Map<T, U> map, final KeyType type) {
    super(new Hashtable<>(map), type);
  }

}
