package com.celeste.library.core.registry.impl;

import com.celeste.library.core.registry.entry.Entry;
import com.celeste.library.core.registry.Registry;
import com.celeste.library.core.registry.nodes.Node;
import com.celeste.library.core.registry.nodes.TreeNode;
import com.celeste.library.core.registry.nodes.impl.LinkedNode;
import com.celeste.library.core.registry.set.KeySet;
import com.celeste.library.core.registry.set.Values;
import com.celeste.library.core.registry.set.EntrySet;
import com.celeste.library.core.registry.type.KeyType;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Getter
@Setter
public class LinkedRegistry<K,V> extends MapRegistry<K,V> implements Registry<K,V> {

  private transient LinkedNode<K,V> eldest;
  private transient LinkedNode<K,V> youngest;

  private boolean accessOrder;

  {
    accessOrder = false;
  }

  public LinkedRegistry(final KeyType type) {
    super(type);
  }

  public LinkedRegistry(int initialCapacity) {
    super(initialCapacity);
  }

  public LinkedRegistry(final KeyType type, int initialCapacity) {
    super(type, initialCapacity);
  }

  public LinkedRegistry(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public LinkedRegistry(final KeyType type, int initialCapacity, float loadFactor) {
    super(type, initialCapacity, loadFactor);
  }

  public LinkedRegistry(final Registry<? extends K, ? extends V> registry) {
    super(registry);
  }

  public LinkedRegistry(final KeyType type, final Registry<? extends K, ? extends V> registry) {
    super(type, registry);
  }

  public LinkedRegistry(final int initialCapacity, final float loadFactor, final boolean accessOrder) {
    super(initialCapacity, loadFactor);
    this.accessOrder = accessOrder;
  }

  public LinkedRegistry(final KeyType type, final int initialCapacity, final float loadFactor, final boolean accessOrder) {
    super(type, initialCapacity, loadFactor);
    this.accessOrder = accessOrder;
  }

  @Override
  public boolean containsValue(final @NotNull Object value) {
    for (LinkedNode<K,V> node = eldest; node != null; node = node.getAfter()) {
      if (Objects.equals(value, node.getValue())) {
        return true;
      }
    }

    return false;
  }

  @Override
  public V get(final @NotNull Object key) {
    final Node<K,V> node = getNode(hash(key), key);
    if (node == null) {
      return null;
    }

    if (accessOrder) {
      afterNodeAccess(node);
    }

    return node.getValue();
  }

  @Override
  public V getOrDefault(final Object key, final V defaultValue) {
    final Node<K,V> node = getNode(hash(key), key);
    if (node == null) {
      return defaultValue;
    }

    if (accessOrder) {
      afterNodeAccess(node);
    }

    return node.getValue();
  }

  @Override
  public void wipe() {
    super.wipe();

    setEldest(null);
    setYoungest(null);
  }

  @Override
  public Set<K> getKeys() {
    final Set<K> keySet = super.getKeys();
    return keySet == null
        ? new KeySet<>(this)
        : keySet;
  }

  @Override
  public Collection<V> getAll() {
    final Collection<V> values = super.getValues();
    return values == null
        ? new Values<>(this)
        : values;
  }

  @Override
  public Set<Entry<K, V>> getEntrySet() {
    final Set<Entry<K, V>> entry = super.getEntrySet();
    return entry == null
        ? (entrySet = new EntrySet<>(this))
        : entry;
  }

  @Override
  public void forEach(@NotNull final BiConsumer<? super K, ? super V> action) {
    for (LinkedNode<K,V> node = getEldest(); node != null; node = node.getAfter()) {
      action.accept(node.getKey(), node.getValue());
    }
  }

  @Override
  public void replaceAll(@NotNull final BiFunction<? super K, ? super V, ? extends V> function) {
    for (LinkedNode<K,V> node = getEldest(); node != null; node = node.getAfter()) {
      node.setValue(function.apply(node.getKey(), node.getValue()));
    }
  }

  private void linkNodeLast(final LinkedNode<K,V> entry) {
    final LinkedNode<K,V> last = youngest;
    setYoungest(entry);

    if (last == null) {
      setEldest(entry);
      return;
    }

    entry.setBefore(last);
    last.setAfter(entry);
  }

  private void transferLinks(final LinkedNode<K,V> src, final LinkedNode<K,V> dst) {
    dst.setBefore(src.getBefore());
    dst.setAfter(src.getAfter());

    final LinkedNode<K,V> before = dst.getBefore();
    final LinkedNode<K,V> after = dst.getAfter();

    if (before == null) {
      setEldest(dst);
    } else {
      before.setAfter(dst);
    }

    if (after == null) {
      setYoungest(dst);
    } else {
      after.setBefore(dst);
    }
  }

  public Node<K,V> newNode(final int hash, final K key, final V value, final Node<K,V> node) {
    final LinkedNode<K,V> entry = new LinkedNode<>(hash, key, value, node);

    linkNodeLast(entry);
    return entry;
  }

  public Node<K,V> replacementNode(final Node<K,V> node, final Node<K,V> next) {
    final LinkedNode<K,V> nodeLinkedNode = (LinkedNode<K,V>) node;
    final LinkedNode<K,V> entry = new LinkedNode<>(nodeLinkedNode.getHash(), nodeLinkedNode.getKey(), nodeLinkedNode.getValue(), next);

    transferLinks(nodeLinkedNode, entry);
    return entry;
  }

  public TreeNode<K,V> newTreeNode(final int hash, final K key, final V value, final Node<K,V> next) {
    final TreeNode<K,V> treeNode = new TreeNode<>(hash, key, value, next);

    linkNodeLast(treeNode);
    return treeNode;
  }

  public TreeNode<K,V> replacementTreeNode(final Node<K,V> node, final Node<K,V> next) {
    final LinkedNode<K,V> entry = (LinkedNode<K,V>) node;
    final TreeNode<K,V> treeNode = new TreeNode<>(entry.getHash(), entry.getKey(), entry.getValue(), next);

    transferLinks(entry, treeNode);
    return treeNode;
  }

  public void afterNodeRemoval(final Node<K,V> node) {
    final LinkedNode<K,V> entry = (LinkedNode<K,V>) node, before = entry.getBefore(), after = entry.getAfter();
    entry.setBefore(entry.getAfter());

    if (before == null) {
      setEldest(after);
    } else {
      before.setAfter(after);
    }

    if (after == null) {
      setYoungest(before);
    } else {
      after.setBefore(before);
    }
  }

  public void afterNodeInsertion(final boolean evict) {
    final LinkedNode<K,V> first = eldest;
    if (evict && first != null) {
      final K key = first.getKey();
      removeNode(hash(key), key, null, false, true);
    }
  }

  public void afterNodeAccess(final Node<K,V> node) {
    final LinkedNode<K,V> last = youngest;
    if (!accessOrder && last == node) {
      return;
    }

    final LinkedNode<K,V> entry = (LinkedNode<K,V>) node, before = entry.getBefore(), after = entry.getAfter();
    entry.setAfter(null);

    if (before == null) {
      setEldest(after);
    } else {
      before.setAfter(after);
    }

    if (after != null) {
      after.setBefore(before);
    } else {
      setEldest(before);
    }

    if (last == null) {
      setEldest(entry);
    } else {
      entry.setBefore(last);
      last.setAfter(entry);
    }

    setYoungest(entry);
    modificationsCount++;
  }

}
