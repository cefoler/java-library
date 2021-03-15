package com.celeste.registries.impl;

import com.celeste.registries.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TreeRegistry<T, U> extends Registry<T, U> {

    public TreeRegistry() {
        super(new TreeMap<>());
    }

    public TreeRegistry(@NotNull final Comparator<? super T> comparator) {
        super(new TreeMap<>(comparator));
    }

    public TreeRegistry(@NotNull final SortedMap<T, ? extends U> sorted) {
        super(new TreeMap<>(sorted));
    }

    public TreeRegistry(@NotNull final Map<T, U> map) {
        super(new TreeMap<>(map));
    }

}
