package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LinkedRegistry<T, U> extends Registry<T, U> {

    public LinkedRegistry() {
        super(new LinkedHashMap<>());
    }

    public LinkedRegistry(final int initialSize) {
        super(new ConcurrentHashMap<>(initialSize));
    }

    public LinkedRegistry(final int initialSize, final float density) {
        super(new LinkedHashMap<>(initialSize, density));
    }

    public LinkedRegistry(final int initialCapacity, final float density, final boolean accessOrder) {
        super(new LinkedHashMap<>(initialCapacity, density, accessOrder));
    }

    public LinkedRegistry(@NotNull final Map<T, U> map) {
        super(new LinkedHashMap<>(map));
    }

}
