package com.celeste.library.core.model.registry.test.entry;

import com.celeste.library.core.model.registry.test.Entry;
import lombok.Data;

import java.io.Serializable;

@Data
public final class SimpleImmutableEntry<K,V> implements Entry<K,V>, Serializable {

  private final K key;
  private final V value;

  public SimpleImmutableEntry(final Entry<? extends K, ? extends V> entry) {
    this.key = entry.getKey();
    this.value = entry.getValue();
  }

  @Override
  public V setValue(final V value) {
    // Immutables cannot change
    return null;
  }

}
