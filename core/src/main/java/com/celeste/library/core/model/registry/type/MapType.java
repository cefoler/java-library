package com.celeste.library.core.model.registry.type;

import com.celeste.library.core.model.registry.builder.MapBuilder;
import com.celeste.library.core.model.registry.Registry;
import com.celeste.library.core.model.registry.impl.*;

public enum MapType {

  MAP,
  LINKED,
  CONCURRENT,
  TABLE,
  TREE,
  WEAK;

  public <T, U> Registry<T, U> get(final MapBuilder builder) {
    switch (this) {
      case TREE:
        return new TreeRegistry<>(builder.getKeyType());
      case LINKED:
        return new LinkedRegistry<>(builder.getInitialCapacity(), builder.getKeyType());
      case CONCURRENT:
        return new ConcurrentRegistry<>(builder.getInitialCapacity(), builder.getKeyType());
      case TABLE:
        return new TableRegistry<>(builder.getInitialCapacity(), builder.getKeyType());
      case WEAK:
        return new WeakRegistry<>(builder.getInitialCapacity(), builder.getKeyType());
      default:
        return new MapRegistry<>(builder.getInitialCapacity(), builder.getKeyType());
    }
  }

}
