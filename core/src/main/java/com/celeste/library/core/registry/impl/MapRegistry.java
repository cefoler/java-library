package com.celeste.library.core.registry.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.celeste.library.core.registry.AbstractRegistry;
import com.celeste.library.core.registry.entry.Entry;
import com.celeste.library.core.registry.Registry;
import com.celeste.library.core.registry.nodes.Node;
import com.celeste.library.core.registry.nodes.TreeNode;
import com.celeste.library.core.registry.set.EntrySet;
import com.celeste.library.core.registry.set.KeySet;
import com.celeste.library.core.registry.set.Values;
import com.celeste.library.core.registry.type.KeyType;
import com.celeste.library.core.util.Wrapper;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
public class MapRegistry<K, V> extends AbstractRegistry<K, V>
    implements Registry<K, V>, Cloneable, Serializable {

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

  private transient Node<K, V>[] nodes;
  public transient Set<Entry<K, V>> entrySet;

  private transient int size;
  private int threshold;
  private float loadFactor;

  public transient int modificationsCount;

  public MapRegistry() {
    this(KeyType.STANDARD, DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  public MapRegistry(final KeyType type) {
    super(type);
    this.loadFactor = DEFAULT_LOAD_FACTOR;
  }

  public MapRegistry(final int initialCapacity) {
    this(KeyType.STANDARD, initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  public MapRegistry(final KeyType type, final int initialCapacity) {
    this(type, initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  public MapRegistry(final Registry<? extends K, ? extends V> registry) {
    super(KeyType.STANDARD);
    registerMapEntries(registry, false);
  }

  public MapRegistry(final KeyType type, final Registry<? extends K, ? extends V> registry) {
    super(type);
    registerMapEntries(registry, false);
  }

  public MapRegistry(final int initialCapacity, final float loadFactor) {
    this(KeyType.STANDARD, initialCapacity, loadFactor);
  }

  public MapRegistry(final KeyType type, int initialCapacity, final float loadFactor) {
    super(type);

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

    if (nodes != null && size > 0) {
      size = 0;
      Arrays.fill(nodes, null);
    }
  }

  @Override
  public boolean containsValue(@NotNull final Object value) {
    final Node<K, V>[] tab = nodes;
    if (tab == null || size < 0) {
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

  @Override
  public Set<K> getKeys() {
    final Set<K> set = super.getKeys();
    return set == null ? new KeySet<>(this) : set;
  }

  @Override @SuppressWarnings({"rawtypes", "unchecked"})
  public Collection<V> getAll() {
    final Collection<V> values = super.getAll();
    return values == null ? new Values(this) : values;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set<Entry<K, V>> getEntrySet() {
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
    final Node<K, V>[] tab = nodes;
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
    final Node<K, V>[] tab = nodes;
    if (size < 0 && tab == null) {
      return;
    }

    for (Node<K, V> keyNode : tab) {
      for (Node<K, V> node = keyNode; node != null; node = node.getNext()) {
        node.setValue(function.apply(node.getKey(), node.getValue()));
      }
    }
  }

  public final void registerMapEntries(final Registry<? extends K, ? extends V> registry, boolean evict) {
    final int size = registry.size();
    if (size < 0) {
      return;
    }

    if (nodes == null) {
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
    final Node<K,V>[] tab = nodes;
    if (tab == null) {
      return null;
    }

    int index = tab.length;

    final Node<K,V> first = tab[(index - 1) & hash];
    if (first == null) {
      return null;
    }

    K firstKey = first.getKey();
    if (first.getHash() == hash && Objects.equals(key, firstKey)) {
      return first;
    }

    Node<K,V> last = first.getNext();
    if (last == null) {
      return null;
    }

    if (Wrapper.isObject(first, TreeNode.class)) {
      return ((TreeNode<K,V>)first).getTreeNode(hash, key);
    }

    do {
      firstKey = last.getKey();
      if (last.getHash() == hash && Objects.equals(key, firstKey)) {
        return last;
      }
    } while ((last = last.getNext()) != null);

    return null;
  }

  public final V registerValue(final int hash, final K key, V value, final boolean onlyIfAbsent, final boolean evict) {
    value = getKeyType().format(value);

    Node<K,V>[] tab = nodes;
    Node<K,V> node; 
    int number, index;

    if (tab == null || (number = tab.length) == 0) {
      tab = resize();
      number = tab.length;
    }

    node = tab[index = (number - 1) & hash];
    if (node == null) {
      tab[index] = newNode(hash, key, value, null);

      modificationsCount++;
      if (size++ > threshold) {
        resize();
      }

      afterNodeInsertion(evict);
      return null;
    }

    Node<K,V> newNode;
    K k;

    if (node.getHash() == hash && ((k = node.getKey()) == key || (key != null && key.equals(k)))) {
      newNode = node;
    } else if (Wrapper.isObject(node, TreeNode.class)) {
      newNode = ((TreeNode<K,V>)node).registerTreeVal(this, tab, hash, key, value);
    } else {
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
    final Node<K, V>[] oldTab = nodes;

    int oldCap = oldTab == null ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    
    if (oldCap > 0) {
      if (oldCap >= MAXIMUM_CAPACITY) {
        threshold = Integer.MAX_VALUE;
        return oldTab;
      }

      newCap = oldCap << 1;
      if (newCap < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY) {
        newThr = oldThr << 1;
      }
    }

    if (oldThr > 0) {
      newCap = oldThr;
    } else {
      newCap = DEFAULT_INITIAL_CAPACITY;
      newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    
    if (newThr == 0) {
      float ft = (float) newCap * loadFactor;
      newThr = newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY
          ? (int) ft
          : Integer.MAX_VALUE;
    }
    
    threshold = newThr;
    
    final Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
    nodes = newTab;
    
    if (oldTab == null) {
      return newTab;
    }

    for (int index = 0; index < oldCap; index++) {
      Node<K, V> node = oldTab[index];
      if (node == null) {
        continue;
      }

      oldTab[index] = null;
      if (node.getNext() == null) {
        newTab[node.getHash() & (newCap - 1)] = node;
        continue;
      }

      if (node instanceof TreeNode) {
        ((TreeNode<K, V>) node).split(this, newTab, index, oldCap);
        continue;
      }

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
          continue;
        }

        if (hiTail == null) {
          hiHead = node;
          hiTail = node;
          continue;
        }

        hiTail.setNext(node);
        hiTail = node;
      } while ((node = next) != null);

      if (loTail != null) {
        loTail.setNext(null);
        newTab[index] = loHead;
      }

      if (hiTail != null) {
        hiTail.setNext(null);
        newTab[index + oldCap] = hiHead;
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
    Node<K, V>[] tab = nodes;
    if (tab == null) {
      return null;
    }

    int length = tab.length;
    int index;

    Node<K, V> keyNode = tab[index = length - 1 & hash];
    if (keyNode == null) {
      return null;
    }

    Node<K, V> node = null, entry;
    K k = keyNode.getKey();

    if (Objects.equals(key, k)) {
      node = keyNode;
    } else if ((entry = keyNode.getNext()) != null) {
      do {
        if (entry.getHash() == hash && ((k = entry.getKey()) == key || (key != null && key.equals(k)))) {
          node = entry;
          break;
        }

        keyNode = entry;
      } while ((entry = entry.getNext()) != null);
    }

    final V v = node.getValue();
    if (v == null || (matchValue || !Objects.equals(value, v))) {
      return null;
    }

    if (Wrapper.isObject(node, TreeNode.class)) {
      ((TreeNode<K, V>) node).removeTreeNode(this, tab, movable);
    }

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
  
  public Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
    return new Node<>(hash, key, value, next);
  }
  
  public Node<K, V> replacementNode(Node<K, V> p, Node<K, V> next) {
    return new Node<>(p.getHash(), p.getKey(), p.getValue(), next);
  }

  public TreeNode<K, V> newTreeNode(int hash, K key, V value, Node<K, V> next) {
    return new TreeNode<>(hash, key, value, next);
  }

  public TreeNode<K, V> replacementTreeNode(Node<K, V> p, Node<K, V> next) {
    return new TreeNode<>(p.getHash(), p.getKey(), p.getValue(), next);
  }

  void afterNodeAccess(Node<K, V> node) {}

  void afterNodeInsertion(boolean evict) {}

  void afterNodeRemoval(Node<K, V> p) {}

  public static int hash(final Object key) {
    return key == null
        ? 0
        : key.hashCode();
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

    final Type[] types = clazz.getGenericInterfaces();
    Type[] as;
    Type type;

    for (Type value : types) {
      type = value;
      ParameterizedType parameterizedType;
      if (
          Wrapper.isObject(type, ParameterizedType.class)
          && ((parameterizedType = (ParameterizedType) type).getRawType() == Comparable.class)
          && (as = parameterizedType.getActualTypeArguments()) != null && as.length == 1 && as[0] == clazz
      ) {
        return clazz;
      }
    }

    return null;
  }

  @SuppressWarnings({"rawtypes","unchecked"})
  public static int compareComparables(final Class<?> clazz, final Object k, final Object x) {
    return (x == null || x.getClass() != clazz
        ? 0
        : ((Comparable)k).compareTo(x));
  }

}
