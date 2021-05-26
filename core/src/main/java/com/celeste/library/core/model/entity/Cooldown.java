package com.celeste.library.core.model.entity;

import com.celeste.library.core.model.registry.Registry;
import com.celeste.library.core.model.registry.impl.TreeRegistry;
import java.util.Set;

/**
 * Creates a new Cooldown that registers the Object and the time in Long.
 *
 * <p>After using isActive method, it automatically
 * deletes the value if the cooldown isn't active</p>
 *
 * @param <T> Object
 */
public final class Cooldown<T> {

  private final Registry<T, Long> cooldown;

  public Cooldown() {
    this.cooldown = new TreeRegistry<>();
  }

  /**
   * Registers the Object into the Cooldown with the provided time in Long
   *
   * @param value T
   * @param time  long
   */
  public Long register(final T value, final long time) {
    return cooldown.register(value, time);
  }

  /**
   * Removes the Object from the Cooldown
   *
   * @param value T
   */
  public Long remove(final T value) {
    return cooldown.remove(value);
  }

  /**
   * Check if the Object is in the cooldown list.
   *
   * @param value T
   * @return Boolean
   */
  public boolean contains(final T value) {
    return cooldown.contains(value);
  }

  public Set<T> getKeys() {
    return cooldown.findKeys();
  }

  /**
   * Check if the cooldown from the Object is currently active.
   *
   * @param value T
   * @return Boolean
   */
  public boolean isActive(final T value) {
    if (!cooldown.contains(value)) {
      return false;
    }

    if (cooldown.find(value) >= System.currentTimeMillis()) {
      return true;
    }

    remove(value);
    return false;
  }

}
