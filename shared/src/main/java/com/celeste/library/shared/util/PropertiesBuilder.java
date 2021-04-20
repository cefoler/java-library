package com.celeste.library.shared.util;

import java.util.Properties;

public final class PropertiesBuilder extends Properties {

  /**
   * @param key Key of the value.
   * @param value Value that will be put on that key.
   *
   * @return PropertiesBuilder
   */
  public PropertiesBuilder with(final String key, final Object value) {
    put(key, value);
    return this;
  }

  /**
   * @return Returns Properties from the Builder.
   */
  public Properties wrap() {
    return this;
  }

  /**
   * Checks it the property exists.
   *
   * @param key Key to get the value
   * @return boolean If exists
   */
  public boolean has(final String key) {
    return containsKey(key);
  }

  /**
   * Sets the properties with that Key on the Properties.
   *
   * @param key Key for the value
   * @param value Property object
   */
  public void set(final String key, final Object value) {
    put(key, value);
  }

}
