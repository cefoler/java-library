package com.celeste.library.core.model.registry.test.nodes;

import com.celeste.library.core.model.registry.test.impl.MapRegistry;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

import static com.celeste.library.core.model.registry.test.impl.MapRegistry.*;

@Getter
@Setter
public final class TreeNode<K, V> extends Node<K, V> {
  
  public TreeNode<K, V> parent;
  public TreeNode<K, V> left;
  public TreeNode<K, V> right;
  public TreeNode<K, V> previous;

  public boolean red;

  public TreeNode(int hash, K key, V val, Node<K, V> next) {
    super(hash, key, val, next);
  }

  final TreeNode<K, V> root() {
    for (TreeNode<K, V> node = this, value; ;) {
      value = node.parent;
      if (value == null) {
        return node;
      }

      node = value;
    }
  }

  static <K, V> void moveRootToFront(Node<K, V>[] tab, TreeNode<K, V> root) {
    int n;
    if (root != null && tab != null && (n = tab.length) > 0) {
      int index = (n - 1) & root.getHash();

      final TreeNode<K, V> first = (TreeNode<K, V>) tab[index];
      if (root == first) {
        return;
      }

      Node<K, V> node;
      tab[index] = root;

      final TreeNode<K, V> previousNode = root.previous;
      if ((node = root.getNext()) != null) {
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
  }

  final TreeNode<K, V> find(int h, Object obj, Class<?> clazz) {
    TreeNode<K, V> node = this;
    do {
      int ph, dir;
      K pk;

      TreeNode<K, V> leftNode = node.left, rightNode = node.right, q;
      if ((ph = node.getHash()) > h) {
        node = leftNode;
      } else if (ph < h) {
        node = rightNode;
      } else if ((pk = node.getKey()) == obj || (obj != null && obj.equals(pk))) {
        return node;
      } else if (leftNode == null) {
        node = rightNode;
      } else if (rightNode == null) {
        node = leftNode;
      } else if ((clazz != null || (clazz = comparableClassFor(obj)) != null) && (dir = compareComparables(clazz, obj, pk)) != 0) {
        node = (dir < 0) ? leftNode : rightNode;
      } else if ((q = rightNode.find(h, obj, clazz)) != null) {
        return q;
      } else {
        node = leftNode;
      }
    } while (node != null);

    return null;
  }

  public final TreeNode<K, V> getTreeNode(int h, Object k) {
    return ((parent != null) ? root() : this).find(h, k, null);
  }

  static int tieBreakOrder(Object a, Object b) {
    int d;
    if (a == null || b == null || (d = a.getClass().getName().compareTo(b.getClass().getName())) == 0) d = (System.identityHashCode(a) <= System.identityHashCode(b) ? -1 : 1); {
      return d;
    }
  }

  public final void treeify(Node<K, V>[] tab) {
    TreeNode<K, V> root = null;
    for (TreeNode<K, V> x = this, next; x != null; x = next) {
      next = (TreeNode<K, V>) x.getNext();
      x.left = x.right = null;
      if (root == null) {
        x.parent = null;
        x.red = false;
        root = x;
      } else {
        K k = x.getKey();
        int h = x.getHash();
        Class<?> kc = null;
        for (TreeNode<K, V> p = root; ; ) {
          int dir, ph;
          K pk = p.getKey();
          if ((ph = p.getHash()) > h)
            dir = -1;
          else if (ph < h)
            dir = 1;
          else if ((kc == null &&
              (kc = comparableClassFor(k)) == null) ||
              (dir = compareComparables(kc, k, pk)) == 0)
            dir = tieBreakOrder(k, pk);

          TreeNode<K, V> xp = p;
          if ((p = (dir <= 0) ? p.left : p.right) == null) {
            x.parent = xp;
            if (dir <= 0)
              xp.left = x;
            else
              xp.right = x;
            root = balanceInsertion(root, x);
            break;
          }
        }
      }
    }
    moveRootToFront(tab, root);
  }

  /**
   * Returns a list of non-TreeNodes replacing those linked from
   * this node.
   */
  final Node<K, V> untreeify(MapRegistry<K, V> map) {
    Node<K, V> hd = null, tl = null;
    for (Node<K, V> q = this; q != null; q = q.getNext()) {
      Node<K, V> p = map.replacementNode(q, null);
      if (tl == null)
        hd = p;
      else
        tl.setNext(p);
      tl = p;
    }
    return hd;
  }

  /**
   * Tree version of putVal.
   */
  public final TreeNode<K, V> putTreeVal(MapRegistry<K, V> map, Node<K, V>[] tab,
                                         int h, K k, V v) {
    Class<?> kc = null;
    boolean searched = false;
    TreeNode<K, V> root = (parent != null) ? root() : this;
    for (TreeNode<K, V> p = root; ; ) {
      int dir, ph;
      K pk;
      if ((ph = p.getHash()) > h)
        dir = -1;
      else if (ph < h)
        dir = 1;
      else if ((pk = p.getKey()) == k || (k != null && k.equals(pk)))
        return p;
      else if ((kc == null &&
          (kc = comparableClassFor(k)) == null) ||
          (dir = compareComparables(kc, k, pk)) == 0) {
        if (!searched) {
          TreeNode<K, V> q, ch;
          searched = true;
          if (((ch = p.left) != null &&
              (q = ch.find(h, k, kc)) != null) ||
              ((ch = p.right) != null &&
                  (q = ch.find(h, k, kc)) != null))
            return q;
        }
        dir = tieBreakOrder(k, pk);
      }

      TreeNode<K, V> xp = p;
      if ((p = (dir <= 0) ? p.left : p.right) == null) {
        Node<K, V> xpn = xp.getNext();
        TreeNode<K, V> x = map.newTreeNode(h, k, v, xpn);
        if (dir <= 0)
          xp.left = x;
        else
          xp.right = x;
        xp.setNext(x);
        x.parent = x.previous = xp;
        if (xpn != null)
          ((TreeNode<K, V>) xpn).previous = x;
        moveRootToFront(tab, balanceInsertion(root, x));
        return null;
      }
    }
  }

  final void removeTreeNode(MapRegistry<K, V> map, Node<K, V>[] tab,
                            boolean movable) {
    int n;
    if (tab == null || (n = tab.length) == 0)
      return;
    int index = (n - 1) & getHash();
    TreeNode<K, V> first = (TreeNode<K, V>) tab[index], root = first, rl;
    TreeNode<K, V> succ = (TreeNode<K, V>) getNext(), pred = previous;
    if (pred == null)
      tab[index] = first = succ;
    else
      pred.setNext(succ);
    if (succ != null)
      succ.previous = pred;
    if (first == null)
      return;
    if (root.parent != null)
      root = root.root();
    if (root == null
        || (movable
        && (root.right == null
        || (rl = root.left) == null
        || rl.left == null))) {
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
      if (lc <= UNTREEIFY_THRESHOLD)
        tab[index] = loHead.untreeify(map);
      else {
        tab[index] = loHead;
        if (hiHead != null) // (else is already treeified)
          loHead.treeify(tab);
      }
    }
    if (hiHead != null) {
      if (hc <= UNTREEIFY_THRESHOLD)
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
