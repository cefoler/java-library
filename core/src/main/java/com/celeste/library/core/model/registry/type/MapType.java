package com.celeste.library.core.model.registry.type;

import com.celeste.library.core.model.registry.Registry;
import com.celeste.library.core.model.registry.impl.ConcurrentRegistry;

public enum MapType {

  CONCURRENT,
  LINKED,
  MAP,
  TABLE,
  TREE,
  WEAK;

  public <T, U> Registry<T, U> get(final KeyType keyType) {
    switch (this) {
      case CONCURRENT: return new ConcurrentRegistry<>(keyType);
    }

    return null;
  }

}
