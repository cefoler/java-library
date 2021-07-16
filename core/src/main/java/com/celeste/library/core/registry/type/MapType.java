package com.celeste.library.core.registry.type;

import com.celeste.library.core.registry.MapBuilder;
import com.celeste.library.core.registry.Registry;
import com.celeste.library.core.registry.impl.LinkedRegistry;
import com.celeste.library.core.registry.impl.MapRegistry;

public enum MapType {

  MAP,
  LINKED,
  CONCURRENT,
  TABLE,
  TREE,
  WEAK;

  public <T, U> Registry<T, U> get(final MapBuilder builder) {
    switch (this) {
      case MAP:
        return new MapRegistry<>(builder.getKeyType(), builder.getInitialCapacity(), builder.getLoadFactor());
      case LINKED:
        return new LinkedRegistry<>(builder.getKeyType(), builder.getInitialCapacity(), builder.getLoadFactor(), builder.isAccessOrder());
      default: return new MapRegistry<>(builder.getKeyType(), builder.getInitialCapacity(), builder.getLoadFactor());
    }
  }

}
