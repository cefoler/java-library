package com.celeste.library.core.registry.nodes;

import com.celeste.library.core.registry.impl.MapRegistry;
import com.celeste.library.core.registry.nodes.impl.LinkedNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class TreeNode<K, V> extends LinkedNode<K, V> {

  private TreeNode<K, V> left;
  private TreeNode<K, V> right;

  private TreeNode<K, V> parent;
  private TreeNode<K, V> previous;

  private boolean red;

  public TreeNode(final int hash, final K key, final V val, final Node<K, V> next) {
    super(hash, key, val, next);
  }

  final TreeNode<K, V> root() {
    for (TreeNode<K, V> node = this, value; ; ) {
      value = node.parent;
      if (value == null) {
        return node;
      }

      node = value;
    }
  }

  public static <K, V> void moveRootToFront(final Node<K, V>[] tab, final TreeNode<K, V> root) {
    int number;
    if (root == null || tab == null || (number = tab.length) <= 0) {
      return;
    }

    final int index = (number - 1) & root.getHash();

    final TreeNode<K, V> first = (TreeNode<K, V>) tab[index];
    if (root == first) {
      return;
    }

    tab[index] = root;

    final Node<K, V> node = root.getNext();
    final TreeNode<K, V> previousNode = root.getPrevious();
    if (node != null) {
      ((TreeNode<K, V>) node).setPrevious(previousNode);
    }

    if (previousNode != null) {
      previousNode.setNext(node);
    }

    if (first != null) {
      first.setPrevious(root);
    }

    root.setNext(first);
    root.setPrevious(null);
  }

  public final TreeNode<K, V> find(final int h, final Object obj, Class<?> clazz) {
    TreeNode<K, V> node = this;

    do {
      final TreeNode<K, V> leftNode = node.getLeft();
      final TreeNode<K, V> rightNode = node.getRight();

      int ph = node.getHash();

      if (ph > h) {
        node = leftNode;
        continue;
      }

      if (ph < h) {
        node = rightNode;
        continue;
      }

      int dir;

      K key = node.getKey();
      if (Objects.equals(obj, key)) {
        return node;
      }

      if (leftNode == null) {
        node = rightNode;
        continue;
      }

      if (rightNode == null) {
        node = leftNode;
        continue;
      }

      if ((clazz != null || (clazz = MapRegistry.comparableClassFor(obj)) != null) && (dir = MapRegistry.compareComparables(clazz, obj, key)) != 0) {
        node = (dir < 0) ? leftNode : rightNode;
        continue;
      }

      final TreeNode<K, V> nodeFound = rightNode.find(h, obj, clazz);
      if (nodeFound != null) {
        return nodeFound;
      }

      node = leftNode;
    } while (node != null);

    return null;
  }

  public final TreeNode<K, V> getTreeNode(final int hash, final Object object) {
    final TreeNode<K, V> node = parent != null ? root() : this;
    return node.find(hash, object, null);
  }

  public static int tieBreakOrder(final Object one, final Object two) {
    return one.getClass().getName().compareTo(two.getClass().getName()) == 0
        ? (System.identityHashCode(one) <= System.identityHashCode(two) ? -1 : 1)
        : 0;
  }

  public final void treeify(final Node<K, V>[] nodes) {
    TreeNode<K, V> root = null;

    for (TreeNode<K, V> node = this, next; node != null; node = next) {
      next = (TreeNode<K, V>) node.getNext();
      node.left = node.right = null;

      if (root == null) {
        node.parent = null;
        node.red = false;

        root = node;
        continue;
      }

      final K key = node.getKey();
      final int hash = node.getHash();

      Class<?> clazz = null;
      for (TreeNode<K, V> treeNode = root; ; ) {
        int treeHash = treeNode.getHash();
        
        final K nodeKey = treeNode.getKey();

        int index;
        if (treeHash > hash) {
          index = -1;
        } else if (treeHash < hash){
          index = 1;
        } else if ((clazz == null && (clazz = MapRegistry.comparableClassFor(key)) == null) || (index = MapRegistry.compareComparables(clazz, key, nodeKey)) == 0) {
          index = tieBreakOrder(key, nodeKey);
        }

        TreeNode<K, V> nodeOne = treeNode;

        final TreeNode<K, V> nodeTwo = treeNode = (index <= 0)
            ? treeNode.left
            : treeNode.right;

        if (nodeTwo != null) {
          continue;
        }

        node.parent = nodeOne;
        if (index <= 0) {
          nodeOne.left = node;
        } else {
          nodeOne.right = node;
        }

        root = balanceInsertion(root, node);
        break;
      }
    }

    moveRootToFront(nodes, root);
  }
  
  public final Node<K, V> untreeify(final MapRegistry<K, V> registry) {
    Node<K, V> nodeOne = null, nodeTwo = null;
    
    for (Node<K, V> node = this; node != null; node = node.getNext()) {
      Node<K, V> parent = registry.replacementNode(node, null);
      if (nodeTwo == null) {
        nodeOne = parent;
        nodeTwo = parent;
        continue;
      }
      
      nodeTwo.setNext(parent);
      nodeTwo = parent;
    }
    
    return nodeOne;
  }
  
  public final TreeNode<K, V> registerTreeVal(MapRegistry<K, V> registry, Node<K, V>[] nodes, int hash, K key, V value) {
    Class<?> clazz = null;
    boolean searched = false;

    final TreeNode<K, V> root = parent != null ? root() : this;
    for (TreeNode<K, V> node = root; ; ) {
      int index = 0;
      
      final int nodeHash = node.getHash();
      if (nodeHash > hash) {
        index = -1;
      } 
      
      if (nodeHash < hash) {
        index = 1;
      }

      final K nodeKey = node.getKey();
      if (Objects.equals(key, nodeKey)) {
        return node;
      } 
      
      if ((clazz == null && (clazz = MapRegistry.comparableClassFor(key)) == null) || (index = MapRegistry.compareComparables(clazz, key, nodeKey)) == 0) {
        if (searched) {
          index = tieBreakOrder(key, nodeKey);
        } else {
          TreeNode<K, V> q, ch;
          searched = true;
          if (((ch = node.left) != null && (q = ch.find(hash, key, clazz)) != null) || ((ch = node.right) != null && (q = ch.find(hash, key, clazz)) != null)) {
            return q;
          }
        }
      }

      TreeNode<K, V> oldNode = node;
      final TreeNode<K, V> treeNode = node = (index <= 0)
          ? node.left
          : node.right;

      if (treeNode != null) {
        continue;
      }

      Node<K, V> xpn = oldNode.getNext();
      TreeNode<K, V> newNode = registry.newTreeNode(hash, key, value, xpn);
      if (index <= 0) {
        oldNode.left = newNode;
      } else {
        oldNode.right = newNode;
      }

      oldNode.setNext(newNode);
      newNode.parent = newNode.previous = oldNode;

      if (xpn != null) {
        ((TreeNode<K, V>) xpn).previous = newNode;
      }

      moveRootToFront(nodes, balanceInsertion(root, newNode));
      return null;
    }
  }

  public final void removeTreeNode(MapRegistry<K, V> map, Node<K, V>[] tab, boolean movable) {
    int length;
    if (tab == null || (length = tab.length) == 0) {
      return;
    }

    final int index = (length - 1) & getHash();
    TreeNode<K, V> first = (TreeNode<K, V>) tab[index], root = first, rl;
    TreeNode<K, V> succ = (TreeNode<K, V>) getNext(), pred = previous;

    if (pred == null) {
      tab[index] = first = succ;
    } else {
      pred.setNext(succ);
    }

    if (succ != null) {
      succ.previous = pred;
    }

    if (first == null) {
      return;
    }

    if (root.parent != null) {
      root = root.root();
    }

    if (root == null || (movable && (root.right == null || (rl = root.left) == null || rl.left == null))) {
      tab[index] = first.untreeify(map);  // too small
      return;
    }

    TreeNode<K, V> p = this, pl = left, pr = right, replacement;
    if (pl != null && pr != null) {
      TreeNode<K, V> s = pr, sl;
      while ((sl = s.left) != null) // find successor
        s = sl;
      boolean c = s.red;
      s.red = p.red;
      p.red = c; // swap colors
      TreeNode<K, V> sr = s.right;
      TreeNode<K, V> pp = p.parent;
      if (s == pr) { // p was s's direct parent
        p.parent = s;
        s.right = p;
      } else {
        TreeNode<K, V> sp = s.parent;
        if ((p.parent = sp) != null) {
          if (s == sp.left)
            sp.left = p;
          else
            sp.right = p;
        }
        if ((s.right = pr) != null)
          pr.parent = s;
      }
      p.left = null;
      if ((p.right = sr) != null)
        sr.parent = p;
      if ((s.left = pl) != null)
        pl.parent = s;
      if ((s.parent = pp) == null)
        root = s;
      else if (p == pp.left)
        pp.left = s;
      else
        pp.right = s;
      if (sr != null)
        replacement = sr;
      else
        replacement = p;
    } else if (pl != null)
      replacement = pl;
    else if (pr != null)
      replacement = pr;
    else
      replacement = p;
    if (replacement != p) {
      TreeNode<K, V> pp = replacement.parent = p.parent;
      if (pp == null)
        root = replacement;
      else if (p == pp.left)
        pp.left = replacement;
      else
        pp.right = replacement;
      p.left = p.right = p.parent = null;
    }

    TreeNode<K, V> r = p.red ? root : balanceDeletion(root, replacement);

    if (replacement == p) {  // detach
      TreeNode<K, V> pp = p.parent;
      p.parent = null;
      if (pp != null) {
        if (p == pp.left)
          pp.left = null;
        else if (p == pp.right)
          pp.right = null;
      }
    }
    if (movable)
      moveRootToFront(tab, r);
  }

  /**
   * Splits nodes in a tree bin into lower and upper tree bins,
   * or untreeifies if now too small. Called only from resize;
   * see above discussion about split bits and indices.
   *
   * @param map   the map
   * @param tab   the table for recording bin heads
   * @param index the index of the table being split
   * @param bit   the bit of hash to split on
   */
  public final void split(MapRegistry<K, V> map, Node<K, V>[] tab, int index, int bit) {
    TreeNode<K, V> b = this;
    // Relink into lo and hi lists, preserving order
    TreeNode<K, V> loHead = null, loTail = null;
    TreeNode<K, V> hiHead = null, hiTail = null;
    int lc = 0, hc = 0;
    for (TreeNode<K, V> e = b, next; e != null; e = next) {
      next = (TreeNode<K, V>) e.getNext();
      e.setNext(null);
      if ((e.getHash() & bit) == 0) {
        if ((e.previous = loTail) == null)
          loHead = e;
        else
          loTail.setNext(e);
        loTail = e;
        ++lc;
      } else {
        if ((e.previous = hiTail) == null)
          hiHead = e;
        else
          hiTail.setNext(e);
        hiTail = e;
        ++hc;
      }
    }

    if (loHead != null) {
      if (lc <= MapRegistry.UNTREEIFY_THRESHOLD)
        tab[index] = loHead.untreeify(map);
      else {
        tab[index] = loHead;
        if (hiHead != null) // (else is already treeified)
          loHead.treeify(tab);
      }
    }
    if (hiHead != null) {
      if (hc <= MapRegistry.UNTREEIFY_THRESHOLD)
        tab[index + bit] = hiHead.untreeify(map);
      else {
        tab[index + bit] = hiHead;
        if (loHead != null)
          hiHead.treeify(tab);
      }
    }
  }

  /* ------------------------------------------------------------ */
  // Red-black tree methods, all adapted from CLR

  static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> root,
                                          TreeNode<K, V> p) {
    TreeNode<K, V> r, pp, rl;
    if (p != null && (r = p.right) != null) {
      if ((rl = p.right = r.left) != null)
        rl.parent = p;
      if ((pp = r.parent = p.parent) == null)
        (root = r).red = false;
      else if (pp.left == p)
        pp.left = r;
      else
        pp.right = r;
      r.left = p;
      p.parent = r;
    }
    return root;
  }

  static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> root,
                                           TreeNode<K, V> p) {
    TreeNode<K, V> l, pp, lr;
    if (p != null && (l = p.left) != null) {
      if ((lr = p.left = l.right) != null)
        lr.parent = p;
      if ((pp = l.parent = p.parent) == null)
        (root = l).red = false;
      else if (pp.right == p)
        pp.right = l;
      else
        pp.left = l;
      l.right = p;
      p.parent = l;
    }
    return root;
  }

  static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root,
                                                TreeNode<K, V> x) {
    x.red = true;
    for (TreeNode<K, V> xp, xpp, xppl, xppr; ; ) {
      if ((xp = x.parent) == null) {
        x.red = false;
        return x;
      } else if (!xp.red || (xpp = xp.parent) == null)
        return root;
      if (xp == (xppl = xpp.left)) {
        if ((xppr = xpp.right) != null && xppr.red) {
          xppr.red = false;
          xp.red = false;
          xpp.red = true;
          x = xpp;
        } else {
          if (x == xp.right) {
            root = rotateLeft(root, x = xp);
            xpp = (xp = x.parent) == null ? null : xp.parent;
          }
          if (xp != null) {
            xp.red = false;
            if (xpp != null) {
              xpp.red = true;
              root = rotateRight(root, xpp);
            }
          }
        }
      } else {
        if (xppl != null && xppl.red) {
          xppl.red = false;
          xp.red = false;
          xpp.red = true;
          x = xpp;
        } else {
          if (x == xp.left) {
            root = rotateRight(root, x = xp);
            xpp = (xp = x.parent) == null ? null : xp.parent;
          }
          if (xp != null) {
            xp.red = false;
            if (xpp != null) {
              xpp.red = true;
              root = rotateLeft(root, xpp);
            }
          }
        }
      }
    }
  }

  static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root, TreeNode<K, V> x) {
    for (TreeNode<K, V> xp, xpl, xpr; ; ) {
      if (x == null || x == root)
        return root;
      else if ((xp = x.parent) == null) {
        x.red = false;
        return x;
      } else if (x.red) {
        x.red = false;
        return root;
      } else if ((xpl = xp.left) == x) {
        if ((xpr = xp.right) != null && xpr.red) {
          xpr.red = false;
          xp.red = true;
          root = rotateLeft(root, xp);
          xpr = (xp = x.parent) == null ? null : xp.right;
        }
        if (xpr == null)
          x = xp;
        else {
          TreeNode<K, V> sl = xpr.left, sr = xpr.right;
          if ((sr == null || !sr.red) &&
              (sl == null || !sl.red)) {
            xpr.red = true;
            x = xp;
          } else {
            if (sr == null || !sr.red) {
              if (sl != null)
                sl.red = false;
              xpr.red = true;
              root = rotateRight(root, xpr);
              xpr = (xp = x.parent) == null ?
                  null : xp.right;
            }
            if (xpr != null) {
              xpr.red = (xp == null) ? false : xp.red;
              if ((sr = xpr.right) != null)
                sr.red = false;
            }
            if (xp != null) {
              xp.red = false;
              root = rotateLeft(root, xp);
            }
            x = root;
          }
        }
      } else { // symmetric
        if (xpl != null && xpl.red) {
          xpl.red = false;
          xp.red = true;
          root = rotateRight(root, xp);
          xpl = (xp = x.parent) == null ? null : xp.left;
        }
        if (xpl == null)
          x = xp;
        else {
          TreeNode<K, V> sl = xpl.left, sr = xpl.right;
          if ((sl == null || !sl.red) &&
              (sr == null || !sr.red)) {
            xpl.red = true;
            x = xp;
          } else {
            if (sl == null || !sl.red) {
              if (sr != null)
                sr.red = false;
              xpl.red = true;
              root = rotateLeft(root, xpl);
              xpl = (xp = x.parent) == null ?
                  null : xp.left;
            }
            if (xpl != null) {
              xpl.red = (xp == null) ? false : xp.red;
              if ((sl = xpl.left) != null)
                sl.red = false;
            }
            if (xp != null) {
              xp.red = false;
              root = rotateRight(root, xp);
            }
            x = root;
          }
        }
      }
    }
  }

}
