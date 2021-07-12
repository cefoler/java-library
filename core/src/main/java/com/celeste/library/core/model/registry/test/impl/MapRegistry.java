package com.celeste.library.core.model.registry.test.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.celeste.library.core.model.registry.test.AbstractRegistry;
import com.celeste.library.core.model.registry.test.Entry;
import com.celeste.library.core.model.registry.test.Registry;
import com.celeste.library.core.model.registry.test.nodes.Node;
import com.celeste.library.core.model.registry.test.nodes.TreeNode;
import com.celeste.library.core.model.registry.test.set.EntrySet;
import com.celeste.library.core.model.registry.test.set.KeySet;
import com.celeste.library.core.model.registry.test.set.Values;
import com.celeste.library.core.util.Wrapper;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class MapRegistry<K, V> extends AbstractRegistry<K, V> implements Registry<K, V>, Cloneable, Serializable {

  public static final int DEFAULT_INITIAL_CAPACITY;
  public static final int MAXIMUM_CAPACITY;
  public static final float DEFAULT_LOAD_FACTOR;
  public static final int TREEIFY_THRESHOLD;
  public static final int UNTREEIFY_THRESHOLD;
  public static final int MIN_TREEIFY_CAPACITY;

  static {
    DEFAULT_INITIAL_CAPACITY = 16;
    MAXIMUM_CAPACITY = 1 << 30;
    DEFAULT_LOAD_FACTOR = 0.75f;
    TREEIFY_THRESHOLD = 8;
    UNTREEIFY_THRESHOLD = 6;
    MIN_TREEIFY_CAPACITY = 64;
  }

  private transient Node<K, V>[] table;
  private transient Set<Entry<K, V>> entrySet;

  private transient int size;
  private transient int modificationsCount;
  private int threshold;
  private float loadFactor;

  public MapRegistry() {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    this.size = DEFAULT_INITIAL_CAPACITY;
    this.threshold = tableSizeFor(DEFAULT_INITIAL_CAPACITY);
    this.modificationsCount = 0;
  }

  public MapRegistry(final int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  public MapRegistry(final Registry<? extends K, ? extends V> registry) {
    registerMapEntries(registry, false);
  }

  public MapRegistry(int initialCapacity, final float loadFactor) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    }

    if (initialCapacity > MAXIMUM_CAPACITY) {
      initialCapacity = MAXIMUM_CAPACITY;
    }

    if (loadFactor <= 0) {
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
    }

    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
  }

  @Override @Nullable
  public V get(@NotNull Object key) {
    final Node<K, V> entry = getNode(hash(key), key);
    return entry == null ? null : entry.getValue();
  }

  @Override
  public boolean containsKey(@NotNull final Object key) {
    return getNode(hash(key), key) != null;
  }

  @Override
  public V register(final K key, @NotNull final V value) {
    return registerValue(hash(key), key, value, false, true);
  }

  @Override
  public void registerAll(final Registry<? extends K, ? extends V> registry) {
    registerMapEntries(registry, true);
  }

  @Override
  public void wipe() {
    modificationsCount++;

    if (table != null && size > 0) {
      size = 0;
      Arrays.fill(table, null);
    }
  }

  @Override
  public Set<K> getKeys() {
    return null;
  }

  @Override
  public boolean containsValue(@NotNull final Object value) {
    final Node<K, V>[] tab = table;
    if (tab == null && size < 0) {
      return false;
    }

    for (Node<K, V> keyNode : tab) {
      for (Node<K, V> node = keyNode; node != null; node = node.getNext()) {
        if (node.getValue() == value) {
          return true;
        }
      }
    }

    return false;
  }

  public Set<K> keySet() {
    final Set<K> set = getKeys();
    return set == null ? new KeySet<>(this) : set;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Collection<V> values() {
    final Collection<V> values = getAll();
    return values == null ? new Values(this) : values;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set<Entry<K, V>> entrySet() {
    return entrySet == null
        ? new EntrySet(this)
        : entrySet;
  }

  @Override
  public V getOrDefault(Object key, V defaultValue) {
    final Node<K, V> node = getNode(hash(key), key);
    return node == null ? defaultValue : node.getValue();
  }

  @Override
  public V putIfAbsent(@NotNull K key, @NotNull V value) {
    return registerValue(hash(key), key, value, true, true);
  }

  @Override
  public boolean replace(K key, V oldValue, V newValue) {
    final Node<K, V> node = getNode(hash(key), key);

    V value;
    if (node != null && ((value = node.getValue()) == oldValue || (value != null && value.equals(oldValue)))) {
      node.setValue(newValue);

      afterNodeAccess(node);
      return true;
    }

    return false;
  }

  @Override
  public V replace(K key, V value) {
    final Node<K, V> node = getNode(hash(key), key);
    if (node != null) {
      final V oldValue = node.getValue();
      node.setValue(value);

      afterNodeAccess(node);
      return oldValue;
    }

    return null;
  }

  @Override
  public V remove(@NotNull final Object key) {
    final Node<K, V> node = removeNode(hash(key), key, null, false, true);
    return node == null ? null : node.getValue();
  }

  @Override
  public void forEach(@NotNull final BiConsumer<? super K, ? super V> action) {
    final Node<K, V>[] tab = table;
    if (size < 0 && tab == null) {
      return;
    }

    for (Node<K, V> keyNode : tab) {
      for (Node<K, V> node = keyNode; node != null; node = node.getNext()) {
        action.accept(node.getKey(), node.getValue());
      }
    }
  }

  @Override
  public void replaceAll(@NotNull final BiFunction<? super K, ? super V, ? extends V> function) {
    final Node<K, V>[] tab = table;
    if (size < 0 && tab == null) {
      return;
    }

    for (Node<K, V> keyNode : tab) {
      for (Node<K, V> node = keyNode; node != null; node = node.getNext()) {
        node.setValue(function.apply(node.getKey(), node.getValue()));
      }
    }
  }

  final int capacity() {
    return table != null
        ? table.length
        : (threshold > 0)
        ? threshold
        : DEFAULT_INITIAL_CAPACITY;
  }

  final void registerMapEntries(final Registry<? extends K, ? extends V> registry, boolean evict) {
    final int size = registry.size();
    if (size < 0) {
      return;
    }

    if (table == null) {
      float ft = ((float) size / loadFactor) + 1.0F;
      int tableSize = ft < (float) MAXIMUM_CAPACITY
          ? (int) ft
          : MAXIMUM_CAPACITY;

      if (tableSize > threshold) {
        threshold = tableSizeFor(tableSize);
      }
    } 
    
    if (size > threshold) {
      resize();
    }

    for (final Entry<? extends K, ? extends V> entry : registry.getEntrySet()) {
      final K key = entry.getKey();
      registerValue(hash(key), key, entry.getValue(), false, evict);
    }
  }

  public final Node<K, V> getNode(final int hash, final Object key) {
    Node<K,V>[] tab = table;
    Node<K,V> first, last;
    int index;
    K k;

    if (tab != null && (index = tab.length) > 0 && (first = tab[(index - 1) & hash]) != null) {
      if (first.getHash() == hash && ((k = first.getKey()) == key || (key != null && key.equals(k)))) {
        return first;
      }

      if ((last = first.getNext()) != null) {
        if (Wrapper.isObject(first, TreeNode.class)) {
          return ((TreeNode<K,V>)first).getTreeNode(hash, key);
        }

        do {
          if (last.getHash() == hash && ((k = last.getKey()) == key || (key != null && key.equals(k)))) {
            return last;
          }
        } while ((last = last.getNext()) != null);
      }
    }

    return null;
  }

  public final V registerValue(final int hash, final K key, final V value, final boolean onlyIfAbsent, final boolean evict) {
    Node<K,V>[] tab = table;
    Node<K,V> node; 
    int number, index;

    final long before = System.nanoTime();
    if (tab == null || (number = tab.length) == 0) {
      tab = resize();
      number = tab.length;
      System.out.println("RESIZE: " + (System.nanoTime() - before));
    }

    if ((node = tab[index = (number - 1) & hash]) == null) {
      tab[index] = newNode(hash, key, value, null);

      modificationsCount++;
      if (size++ > threshold) {
        final long a = System.nanoTime();
        resize();
        System.out.println("RESIZE2: " + (System.nanoTime() - a));
      }

      afterNodeInsertion(evict);
      return null;
    }

    Node<K,V> newNode;
    K k;

    final long a = System.nanoTime();
    if (node.getHash() == hash && ((k = node.getKey()) == key || (key != null && key.equals(k)))) {
      newNode = node;
      System.out.println("FIRST IF: " + (System.nanoTime() - a));
    } else if (Wrapper.isObject(node, TreeNode.class)) {
      final long b = System.nanoTime();
      newNode = ((TreeNode<K,V>)node).putTreeVal(this, tab, hash, key, value);
      System.out.println("SECOND IF: " + (System.nanoTime() - b));
    } else {
      final long c = System.nanoTime();
      for (int binCount = 0; ; ++binCount) {
        if ((newNode = node.getNext()) == null) {
          node.setNext(newNode(hash, key, value, null));
          if (binCount >= TREEIFY_THRESHOLD - 1) {
            treeifyBin(tab, hash);
          }

          break;
        }
        if (newNode.getHash() == hash && ((k = newNode.getKey()) == key || (key != null && key.equals(k)))) {
          break;
        }

        node = newNode;
        System.out.println("THIRD IF: " + (System.nanoTime() - c));
      }
    }

    if (newNode != null) {
      final V oldValue = newNode.getValue();
      if (!onlyIfAbsent || oldValue == null) {
        newNode.setValue(value);
      }

      afterNodeAccess(newNode);
      return oldValue;
    }
    
    modificationsCount++;
    if (size++ > threshold) {
      resize();
    }
    
    afterNodeInsertion(evict);
    return null;
  }

  @SuppressWarnings({"unchecked"})
  public final Node<K, V>[] resize() {
    final Node<K, V>[] oldTab = table;

    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    
    if (oldCap > 0) {
      if (oldCap >= MAXIMUM_CAPACITY) {
        threshold = Integer.MAX_VALUE;
        return oldTab;
      }

      if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY) {
        newThr = oldThr << 1;
      }
    } else if (oldThr > 0) {
      newCap = oldThr;
    } else {
      newCap = DEFAULT_INITIAL_CAPACITY;
      newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    
    if (newThr == 0) {
      float ft = (float) newCap * loadFactor;
      newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ? (int) ft : Integer.MAX_VALUE);
    }
    
    threshold = newThr;
    
    final Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
    table = newTab;
    
    if (oldTab == null) {
      return newTab;
    }

    for (int i = 0; i < oldCap; ++i) {
      Node<K, V> node = oldTab[i];
      if (node == null) {
        continue;
      }

      oldTab[i] = null;
      if (node.getNext() == null) {
        newTab[node.getHash() & (newCap - 1)] = node;
      } else if (node instanceof TreeNode) {
        ((TreeNode<K, V>) node).split(this, newTab, i, oldCap);
      } else {
        Node<K, V> loHead = null, loTail = null;
        Node<K, V> hiHead = null, hiTail = null;
        Node<K, V> next;

        do {
          next = node.getNext();
          if ((node.getHash() & oldCap) == 0) {
            if (loTail == null) {
              loHead = node;
            } else {
              loTail.setNext(node);
            }

            loTail = node;
          } else {
            if (hiTail == null) {
              hiHead = node;
            }
            else {
              hiTail.setNext(node);
            }

            hiTail = node;
          }
        } while ((node = next) != null);

        if (loTail != null) {
          loTail.setNext(null);
          newTab[i] = loHead;
        }

        if (hiTail != null) {
          hiTail.setNext(null);
          newTab[i + oldCap] = hiHead;
        }
      }
    }

    return oldTab;
  }

  public final void treeifyBin(final Node<K, V>[] tab, final int hash) {
    int n, index;
    Node<K,V> node;
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY) {
      resize();
      return;
    }

    if ((node = tab[index = (n - 1) & hash]) != null) {
      TreeNode<K,V> hd = null, tl = null;
      do {
        TreeNode<K,V> p = replacementTreeNode(node, null);
        if (tl == null)
          hd = p;
        else {
          p.setPrevious(tl);
          tl.setNext(p);
        }
        tl = p;
      } while ((node = node.getNext()) != null);
      if ((tab[index] = hd) != null) {
        hd.treeify(tab);
      }
    }
  }

  public final Node<K, V> removeNode(int hash, Object key, Object value, boolean matchValue, boolean movable) {
    Node<K, V>[] tab = table;
    Node<K, V> keyNode = null;
    int n, index = 0;
    if (tab == null && (n = tab.length) < 0 && (keyNode = tab[index = (n - 1) & hash]) == null) {
      return null;
    }

    Node<K, V> node = null, entry;
    K k;
    V v;
    if (keyNode.getHash() == hash && ((k = keyNode.getKey()) == key || (key != null && key.equals(k)))) {
      node = keyNode;
    } else if ((entry = keyNode.getNext()) != null) {
      //if (p instanceof TreeNode) {
      //  node = ((TreeNode<K, V>) p).getTreeNode(hash, key);
      //} else {
        do {
          if (entry.getHash() == hash && ((k = entry.getKey()) == key || (key != null && key.equals(k)))) {
            node = entry;
            break;
          }
          keyNode = entry;
        } while ((entry = entry.getNext()) != null);
      //}
    }

    if (node != null && (!matchValue || (v = node.getValue()) == value || (value != null && value.equals(v)))) {
      //if (node instanceof TreeNode) ((TreeNode<K, V>) node).removeTreeNode(this, tab, movable);
      if (node == keyNode) {
        tab[index] = node.getNext();
      } else {
        keyNode.setNext(node.getNext());
      }

      modificationsCount++;
      size--;

      afterNodeRemoval(node);
      return node;
    }

    return null;
  }


  @Override
  public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
    final int hash = hash(key);
    Node<K, V>[] tab = table;
    Node<K, V> first;
    int n, i;
    int binCount = 0;
    TreeNode<K, V> t = null;
    Node<K, V> old = null;
    if (size > threshold || tab == null || (n = tab.length) == 0) {
      n = (tab = resize()).length;
    }

    if ((first = tab[i = (n - 1) & hash]) != null) {
      if (first instanceof TreeNode) {
        old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
      } else {
        Node<K, V> e = first;
        K k;
        do {
          if (e.getHash() == hash && ((k = e.getKey()) == key || (key != null && key.equals(k)))) {
            old = e;
            break;
          }
          binCount++;
        } while ((e = e.getNext()) != null);
      }

      V oldValue;
      if (old != null && (oldValue = old.getValue()) != null) {
        afterNodeAccess(old);
        return oldValue;
      }
    }

    final V value = mappingFunction.apply(key);
    if (value == null) {
      return null;
    }

    if (old != null) {
      old.setValue(value);
      afterNodeAccess(old);
      return value;
    }

    if (t != null) {
      t.putTreeVal(this, tab, hash, key, value);
    } else {
      tab[i] = newNode(hash, key, value, first);
      if (binCount >= TREEIFY_THRESHOLD - 1) {
        treeifyBin(tab, hash);
      }
    }

    modificationsCount++;
    size++;
    afterNodeInsertion(true);
    return value;
  }

  public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    final int hash = hash(key);

    V oldValue;
    Node<K, V> node = getNode(hash, key);
    if (node != null && (oldValue = node.getValue()) != null) {
      final V value = remappingFunction.apply(key, oldValue);

      if (value != null) {
        node.setValue(value);

        afterNodeAccess(node);
        return value;
      }

      removeNode(hash, key, null, false, true);
    }

    return null;
  }

  @Override
  public V compute(@NotNull K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    final int hash = hash(key);
    Node<K, V>[] tab = table;
    Node<K, V> first;
    int n, i;
    int binCount = 0;
    TreeNode<K, V> t = null;
    Node<K, V> old = null;
    if (size > threshold || (tab == null || (n = tab.length) == 0)) {
      n = (tab = resize()).length;
    }

    if ((first = tab[i = (n - 1) & hash]) != null) {
      if (first instanceof TreeNode) {
        old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
      } else {
        K k = first.getKey();
        do {
          if (first.getHash() == hash && (k == key || key.equals(k))) {
            old = first;
            break;
          }

          binCount++;
        } while ((first = first.getNext()) != null);
      }
    }

    V oldValue = (old == null) ? null : old.getValue();
    V value = remappingFunction.apply(key, oldValue);
    if (old != null) {
      if (value != null) {
        old.setValue(value);
        afterNodeAccess(old);
      } else {
        removeNode(hash, key, null, false, true);
      }
    } else if (value != null) {
      if (t != null) {
        t.putTreeVal(this, tab, hash, key, value);
      } else {
        tab[i] = newNode(hash, key, value, first);
        if (binCount >= TREEIFY_THRESHOLD - 1) {
          treeifyBin(tab, hash);
        }
      }

      modificationsCount++;
      size++;
      afterNodeInsertion(true);
    }

    return value;
  }

  @Override
  public V merge(@NotNull K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
    int hash = hash(key);
    Node<K, V>[] tab;
    Node<K, V> first;
    int n, i;
    int binCount = 0;
    TreeNode<K, V> t = null;
    Node<K, V> old = null;
    if (size > threshold || (tab = table) == null ||
        (n = tab.length) == 0)
      n = (tab = resize()).length;
    if ((first = tab[i = (n - 1) & hash]) != null) {
      if (first instanceof TreeNode)
        old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
      else {
        Node<K, V> e = first;
        K k;
        do {
          if (e.getHash() == hash &&
              ((k = e.getKey()) == key || (key != null && key.equals(k)))) {
            old = e;
            break;
          }
          ++binCount;
        } while ((e = e.getNext()) != null);
      }
    }
    if (old != null) {
      V v;
      if (old.getValue() != null)
        v = remappingFunction.apply(old.getValue(), value);
      else
        v = value;
      if (v != null) {
        old.setValue(v);
        afterNodeAccess(old);
      } else
        removeNode(hash, key, null, false, true);
      return v;
    }
    if (value != null) {
      if (t != null)
        t.putTreeVal(this, tab, hash, key, value);
      else {
        tab[i] = newNode(hash, key, value, first);
        if (binCount >= TREEIFY_THRESHOLD - 1)
          treeifyBin(tab, hash);
      }
      modificationsCount++;
      size++;
      afterNodeInsertion(true);
    }
    return value;
  }
  
  /* ------------------------------------------------------------ */
  // LinkedHashMap support
  
  Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
    return new Node<>(hash, key, value, next);
  }
  
  public Node<K, V> replacementNode(Node<K, V> p, Node<K, V> next) {
    return new Node<>(p.getHash(), p.getKey(), p.getValue(), next);
  }

  public TreeNode<K, V> newTreeNode(int hash, K key, V value, Node<K, V> next) {
    return new TreeNode<>(hash, key, value, next);
  }

  TreeNode<K, V> replacementTreeNode(Node<K, V> p, Node<K, V> next) {
    return new TreeNode<>(p.getHash(), p.getKey(), p.getValue(), next);
  }

  void afterNodeAccess(Node<K, V> node) {}

  void afterNodeInsertion(boolean evict) {}

  void afterNodeRemoval(Node<K, V> p) {}

  public static int hash(final Object key) {
    final long before = System.nanoTime();
    if (key == null) {
      return 0;
    }

    final int h = key.hashCode();
    System.out.println("HASH: " + (System.nanoTime() - before));
    return h;
  }

  private static int tableSizeFor(int capacity) {
    capacity = capacity - 1;
    return capacity < 0
        ? 1 : capacity >= MAXIMUM_CAPACITY
        ? MAXIMUM_CAPACITY : capacity + 1;
  }

  public static Class<?> comparableClassFor(final Object comparableClazz) {
    if (!Wrapper.isObject(comparableClazz, Comparable.class)) {
       return null;
    }

    final Class<?> clazz = comparableClazz.getClass();
    if (Wrapper.isString(clazz)) {
      return clazz;
    }

    Type[] types = clazz.getGenericInterfaces(), as;
    Type type;
    ParameterizedType parameterizedType;

    for (Type value : types) {
      type = value;
      if (Wrapper.isObject(type, ParameterizedType.class) && ((parameterizedType = (ParameterizedType) type).getRawType() == Comparable.class)
          && (as = parameterizedType.getActualTypeArguments()) != null && as.length == 1 && as[0] == clazz)
        return clazz;
    }

    return null;
  }

  @SuppressWarnings({"rawtypes","unchecked"}) // for cast to Comparable
  public static int compareComparables(final Class<?> clazz, final Object k, final Object x) {
    return (x == null || x.getClass() != clazz ? 0 : ((Comparable)k).compareTo(x));
  }

}
