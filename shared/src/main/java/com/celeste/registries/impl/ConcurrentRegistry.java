package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentRegistry<T, U> extends Registry<T, U> {

    public ConcurrentRegistry() {
        super(new ConcurrentHashMap<>());
    }

    public ConcurrentRegistry(final int initialSize) {
        super(new ConcurrentHashMap<>(initialSize));
    }

    public ConcurrentRegistry(final int initialSize, final float density) {
        super(new ConcurrentHashMap<>(initialSize, density));
    }

    public ConcurrentRegistry(final int initialSize, final float density, final int threads) {
        super(new ConcurrentHashMap<>(initialSize, density, threads));
    }

    public ConcurrentRegistry(@NotNull final Map<T, U> map) {
        super(new ConcurrentHashMap<>(map));
    }

}
