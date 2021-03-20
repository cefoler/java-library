package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;
import java.util.Map;

public final class TableRegistry<T, U> extends Registry<T, U> {

  /**
   * Creates a new TableRegistry
   */
  public TableRegistry() {
    super(new Hashtable<>());
  }

  /**
   * Creates a new TableRegistry with specific initialSize
   *
   * @param initialSize int
   */
  public TableRegistry(final int initialSize) {
    super(new Hashtable<>(initialSize));
  }

  /**
   * Creates a new TableRegistry with specific initial size and density
   *
   * @param initialSize int
   * @param density float
   */
  public TableRegistry(final int initialSize, final float density) {
    super(new Hashtable<>(initialSize, density));
  }

  /**
   * Creates a new TableRegistry from another map
   *
   * @param map Map<T, U>
   */
  public TableRegistry(@NotNull final Map<T, U> map) {
    super(new Hashtable<>(map));
  }

}
