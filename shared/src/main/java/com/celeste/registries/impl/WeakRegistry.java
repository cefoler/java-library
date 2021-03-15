package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public class WeakRegistry<T, U> extends Registry<T, U> {

    public WeakRegistry() {
        super(new WeakHashMap<>());
    }

    public WeakRegistry(final int initialSize) {
        super(new WeakHashMap<>(initialSize));
    }

    public WeakRegistry(final int initialSize, final float density) {
        super(new WeakHashMap<>(initialSize, density));
    }

    public WeakRegistry(@NotNull final Map<T, U> map) {
        super(new WeakHashMap<>(map));
    }

}
