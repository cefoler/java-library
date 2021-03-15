package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;
import java.util.Map;

public class TableRegistry<T, U> extends Registry<T, U> {

    public TableRegistry() {
        super(new Hashtable<>());
    }

    public TableRegistry(final int initialSize) {
        super(new Hashtable<>(initialSize));
    }

    public TableRegistry(final int initialSize, final float density) {
        super(new Hashtable<>(initialSize, density));
    }

    public TableRegistry(@NotNull final Map<T, U> map) {
        super(new Hashtable<>(map));
    }

}
