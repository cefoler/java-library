package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MapRegistry<T, U> extends Registry<T, U> {

    public MapRegistry() {
        super(new HashMap<>());
    }

    public MapRegistry(final int initialSize) {
        super(new HashMap<>(initialSize));
    }

    public MapRegistry(final int initialSize, final float density) {
        super(new HashMap<>(initialSize, density));
    }

    public MapRegistry(@NotNull final Map<T, U> map) {
        super(new HashMap<>(map));
    }

}
