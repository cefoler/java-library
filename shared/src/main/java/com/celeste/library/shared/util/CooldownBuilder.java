package com.celeste.library.shared.util;

import com.celeste.library.shared.model.registry.Registry;
import com.celeste.library.shared.model.registry.impl.LinkedRegistry;
import sun.rmi.server.UnicastServerRef2;

/**
 * Creates a new CooldownBuilder that registers
 * the Object and the time in Long.
 *
 * <p>After using isActive method, it automatically
 * deletes the value if the cooldown isn't active</p>
 *
 * @param <T> Object
 */
public class CooldownBuilder<T> {

  private final Registry<T, Long> cooldown;

  public CooldownBuilder() {
    this.cooldown = new LinkedRegistry<>();
  }

  /**
   * Registers the Object into the Cooldown
   * with the provided time in Long
   *
   * @param value T
   * @param time long
   */
  private void register(final T value, final long time) {
    cooldown.register(value, time);
  }

  /**
   * Removes the Object from the Cooldown
   * @param value T
   */
  private void remove(final T value) {
    cooldown.remove(value);
  }

  /**
   * Check if the Object is in the cooldown list.
   * @param value T
   *
   * @return Boolean
   */
  private boolean contains(final T value) {
    return cooldown.contains(value);
  }

  /**
   * Check if the cooldown from the Object
   * is currently active.
   * @param value T
   *
   * @return Boolean
   */
  private boolean isActive(final T value) {
    if (!cooldown.contains(value)) return false;
    if (cooldown.get(value) >= System.currentTimeMillis()) return true;

    remove(value);
    return false;
  }

}
