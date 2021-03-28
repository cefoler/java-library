package com.celeste.model.registry.impl;

import com.celeste.model.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;
import java.util.Map;

public final class TableRegistry<T, U> extends Registry<T, U> {

  /**
   * Creates a new TableAbstractRegistry
   */
  public TableRegistry() {
    super(new Hashtable<>());
  }

  /**
   * Creates a new TableAbstractRegistry with specific initialSize
   *
   * @param initialSize int
   */
  public TableRegistry(final int initialSize) {
    super(new Hashtable<>(initialSize));
  }

  /**
   * Creates a new TableAbstractRegistry with specific initial size and density
   *
   * @param initialSize int
   * @param density float
   */
  public TableRegistry(final int initialSize, final float density) {
    super(new Hashtable<>(initialSize, density));
  }

  /**
   * Creates a new TableAbstractRegistry from another map
   *
   * @param map Map<T, U>
   */
  public TableRegistry(@NotNull final Map<T, U> map) {
    super(new Hashtable<>(map));
  }

}
