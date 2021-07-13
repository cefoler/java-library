package com.celeste.library.core.registry.entry.impl;

import com.celeste.library.core.registry.entry.Entry;
import lombok.Data;

import java.io.Serializable;

@Data
public final class SimpleImmutableEntry<K, V> implements Entry<K, V>, Serializable {

  private final K key;
  private final V value;

  public SimpleImmutableEntry(final Entry<? extends K, ? extends V> entry) {
    this.key = entry.getKey();
    this.value = entry.getValue();
  }

  @Override @Deprecated
  public V setValue(final V value) {
    // Immutables cannot change
    return null;
  }

}
