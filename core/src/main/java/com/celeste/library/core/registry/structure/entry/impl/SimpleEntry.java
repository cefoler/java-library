package com.celeste.library.core.registry.structure.entry.impl;

import com.celeste.library.core.registry.structure.entry.Entry;
import lombok.Data;

import java.io.Serializable;

@Data
public final class SimpleEntry<K, V> implements Entry<K, V>, Serializable {

  private final K key;
  private V value;

  public SimpleEntry(final Entry<? extends K, ? extends V> entry) {
    this.key = entry.getKey();
    this.value = entry.getValue();
  }

  public V setValue(final V value) {
    final V oldValue = this.value;

    this.value = value;
    return oldValue;
  }

}
