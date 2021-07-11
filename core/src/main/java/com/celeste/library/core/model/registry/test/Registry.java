package com.celeste.library.core.model.registry.test;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Registry<K,V> {

  int size();

  boolean isEmpty();

  boolean containsKey(final Object key);

  boolean containsValue(final Object value);

  V get(final Object key);

  V register(final K key, final V value);

  V remove(final Object key);

  void registerAll(Registry<? extends K, ? extends V> registry);

  void wipe();

  Set<K> getKeys();

  Collection<V> getAll();

  Set<Entry<K, V>> getEntrySet();

  default V getOrDefault(Object key, V defaultValue) {
    final V value = get(key);
    return value != null || containsKey(key)
        ? value
        : defaultValue;
  }

  default void forEach(@NotNull final BiConsumer<? super K, ? super V> action) {
    for (final Entry<K, V> entry : getEntrySet()) {
      action.accept(entry.getKey(), entry.getValue());
    }
  }

  default void replaceAll(@NotNull final BiFunction<? super K, ? super V, ? extends V> function) {
    for (final Entry<K, V> entry : getEntrySet()) {
      try {
        V value = entry.getValue();
        value = function.apply(entry.getKey(), value);

        entry.setValue(value);
      } catch (IllegalStateException exception) {
        throw new ConcurrentModificationException("The replaceAll has been used while the EntrySet is being modified in another method. Details: " + exception.getMessage());
      }
    }
  }

  default V putIfAbsent(@NotNull final K key, @NotNull final V value) {
    final V oldValue = get(key);
    if (oldValue != null) {
      return oldValue;
    }

    return register(key, value);
  }

  default boolean replace(final K key, final V oldValue, final V newValue) {
    if (!containsKey(key) || oldValue.equals(newValue)) {
      return false;
    }

    register(key, newValue);
    return true;
  }

  default V replace(final K key, final V value) {
    if (containsKey(key)) {
      return register(key, value);
    }

    return get(key);
  }

  @Nullable
  default V computeIfAbsent(final K key, @NotNull final Function<? super K, ? extends V> mappingFunction) {
    if (containsKey(key)) {
      return null;
    }

    final V newValue = mappingFunction.apply(key);
    if (newValue != null) {
      register(key, newValue);
      return newValue;
    }

    return null;
  }

  @Nullable
  default V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    if (!containsKey(key)) {
      return null;
    }

    final V newValue = remappingFunction.apply(key, get(key));
    if (newValue != null) {
      register(key, newValue);
      return newValue;
    }

    remove(key);
    return null;
  }

  @Nullable
  default V compute(@NotNull final K key, @NotNull final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    if (!containsKey(key)) {
      return null;
    }

    final V newValue = remappingFunction.apply(key, get(key));

    register(key, newValue);
    return newValue;
  }

  @Nullable
  default V merge(@NotNull final K key, @NotNull final V value, @NotNull final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
    final V oldValue = get(key);
    final V newValue = oldValue == null
        ? value
        : remappingFunction.apply(oldValue, value);

    if (newValue == null) {
      remove(key);
      return null;
    }

    register(key, newValue);
    return newValue;
  }

}
