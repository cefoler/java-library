package com.celeste.library.core.model.entity;

import com.celeste.library.core.model.registry.impl.TreeRegistry;

/**
 * Creates a new Cooldown that registers the Object and the time in Long.
 *
 * <p>After using isActive method, it automatically
 * deletes the value if the cooldown isn't active</p>
 *
 * @param <T> Object
 */
public class Cooldown<T> extends TreeRegistry<T, Long> {

  /**
   * Check if the cooldown from the Object is currently active.
   *
   * @param value T
   * @return Boolean
   */
  public boolean isActive(final T value) {
    if (!contains(value)) {
      return false;
    }

    if (find(value) >= System.currentTimeMillis()) {
      return true;
    }

    remove(value);
    return false;
  }

}
