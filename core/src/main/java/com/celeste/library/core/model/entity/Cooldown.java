package com.celeste.library.core.model.entity;

import com.celeste.library.core.model.registry.impl.TreeRegistry;
import com.celeste.library.core.util.Validation;

/**
 * Creates a new Cooldown that registers the Object and the time in Long.
 *
 * <p>After using isActive method, it automatically
 * deletes the value if the cooldown isn't active</p>
 *
 * @param <T> Object
 */
public class Cooldown<T> extends TreeRegistry<T, Long> {

  public boolean isActive(final T key) {
    if (!contains(key)) {
      return false;
    }

    if (Validation.notNull(get(key)) >= System.currentTimeMillis()) {
      return true;
    }

    remove(key);
    return false;
  }

}
