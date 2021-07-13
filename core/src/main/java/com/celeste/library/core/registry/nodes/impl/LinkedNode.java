package com.celeste.library.core.registry.nodes.impl;

import com.celeste.library.core.registry.nodes.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkedNode<K,V> extends Node<K,V> {

  private LinkedNode<K,V> before, after;

  public LinkedNode(final int hash, final K key, final V value, final Node<K,V> next) {
    super(hash, key, value, next);
  }

}
