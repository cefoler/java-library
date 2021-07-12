package com.celeste.library.core.registry.entry;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface Entry<K,V> {

  K getKey();

  V getValue();

  V setValue(V value);

  static <K extends Comparable<? super K>, V> Comparator<Entry<K,V>> comparingByKey() {
    return Comparator.comparing(Entry::getKey);
  }

  static <K, V extends Comparable<? super V>> Comparator<Entry<K,V>> comparingByValue() {
    return Comparator.comparing(Entry::getValue);
  }

  static <K, V> Comparator<Entry<K, V>> comparingByKey(@NotNull Comparator<? super K> comparator) {
    return (c1, c2) -> comparator.compare(c1.getKey(), c2.getKey());
  }

  static <K, V> Comparator<Entry<K, V>> comparingByValue(@NotNull Comparator<? super V> comparator) {
    return (c1, c2) -> comparator.compare(c1.getValue(), c2.getValue());
  }

}
