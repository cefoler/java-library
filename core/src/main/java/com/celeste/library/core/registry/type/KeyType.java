package com.celeste.library.core.registry.type;

import com.celeste.library.core.util.Wrapper;

public enum KeyType {

  LOWER_CASE,
  UPPER_CASE,
  STANDARD;

  public String convert(final String value) {
    switch (this) {
      case UPPER_CASE: return value.toUpperCase();
      case LOWER_CASE: return value.toLowerCase();
      case STANDARD: return value;
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public <T> T format(T key) {
    if (Wrapper.isString(key)) {
      key = (T) this.convert((String) key);
    }

    return key;
  }

}
