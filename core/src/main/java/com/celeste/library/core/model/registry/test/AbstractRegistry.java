package com.celeste.library.core.model.registry.test;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@ToString
@EqualsAndHashCode
public abstract class AbstractRegistry<K,V> implements Registry<K,V> {

  public int size() {
    return getEntrySet().size();
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public boolean containsValue(@NotNull final Object value) {
    for (Entry<K, V> entry : getEntrySet()) {
      if (value.equals(entry.getValue())) {
        return true;
      }
    }

    return false;
  }

  public boolean containsKey(@NotNull final Object key) {
    for (Entry<K, V> entry : getEntrySet()) {
      if (key.equals(entry.getKey())) {
        return true;
      }
    }

    return false;
  }

  public V get(@NotNull Object key) {
    for (Entry<K, V> entry : getEntrySet()) {
      if (key.equals(entry.getKey())) {
        return entry.getValue();
      }
    }

    return null;
  }

  public V register(K key, V value) {
    throw new UnsupportedOperationException();
  }

  public V remove(@NotNull Object key) {
    final Iterator<Entry<K,V>> iterator = getEntrySet().iterator();

    Entry<K,V> correctEntry = null;
    while (correctEntry == null && iterator.hasNext()) {
      final Entry<K,V> entry = iterator.next();
      if (key.equals(entry.getKey())) {
        correctEntry = entry;
      }
    }

    V oldValue = null;
    if (correctEntry !=null) {
      oldValue = correctEntry.getValue();
      iterator.remove();
    }

    return oldValue;
  }

  public void putAll(Registry<? extends K, ? extends V> registry) {
    for (final Entry<? extends K, ? extends V> entry : registry.getEntrySet()) {
      register(entry.getKey(), entry.getValue());
    }
  }

  public void wipe() {
    getEntrySet().clear();
  }

  transient Set<K> keySet;
  transient Collection<V> values;

  public Set<K> keySet() {
    if (keySet != null) {
      return keySet;
    }

    this.keySet = new AbstractSet<K>() {
      public Iterator<K> iterator() {
        return new Iterator<K>() {
          private final Iterator<Entry<K,V>> iterator = getEntrySet().iterator();

          public boolean hasNext() {
            return iterator.hasNext();
          }

          public K next() {
            return iterator.next().getKey();
          }

          public void remove() {
            iterator.remove();
          }
        };
      }

      public int size() {
        return AbstractRegistry.this.size();
      }

      public boolean isEmpty() {
        return AbstractRegistry.this.isEmpty();
      }

      public void clear() {
        AbstractRegistry.this.wipe();
      }

      public boolean contains(Object key) {
        return AbstractRegistry.this.containsKey(key);
      }
    };

    return keySet;
  }

  public Collection<V> getAll() {
    if (values != null) {
      return values;
    }

    this.values = new AbstractCollection<V>() {
      public Iterator<V> iterator() {
        return new Iterator<V>() {
          private final Iterator<Entry<K,V>> iterator = getEntrySet().iterator();

          public boolean hasNext() {
            return iterator.hasNext();
          }

          public V next() {
            return iterator.next().getValue();
          }

          public void remove() {
            iterator.remove();
          }
        };
      }

      public int size() {
        return AbstractRegistry.this.size();
      }

      public boolean isEmpty() {
        return AbstractRegistry.this.isEmpty();
      }

      public void clear() {
        AbstractRegistry.this.wipe();
      }

      public boolean contains(Object v) {
        return AbstractRegistry.this.containsValue(v);
      }
    };

    return values;
  }

}