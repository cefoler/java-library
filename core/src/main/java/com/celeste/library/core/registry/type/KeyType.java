package com.celeste.library.core.registry.type;

import com.celeste.library.core.util.Wrapper;
import org.jetbrains.annotations.NotNull;

public enum KeyType {

  LOWER_CASE,
  UPPER_CASE,
  STANDARD;

  public String convert(@NotNull final String value) {
    switch (this) {
      case UPPER_CASE:
        return value.toUpperCase();
      case LOWER_CASE:
        return value.toLowerCase();
      case STANDARD:
        return value;
      default:
        return null;
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T format(final T key) {
    if (Wrapper.isString(key)) {
      return (T) convert((String) key);
    }

    return key;
  }

}
