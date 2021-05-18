package com.celeste.library.spigot.model.paginator;

import com.celeste.library.spigot.model.paginator.impl.MenuPaginatorImpl;
import com.celeste.library.spigot.model.paginator.impl.PaginatorImpl;
import java.util.Set;

/**
 * The Paginator interface is the main creator for paginators.
 *
 * <p>Here you can access universal methods for
 * each of the paginators</p>
 *
 * <p>Type of paginators: {@link PaginatorImpl} for Object paginator and
 * {@link MenuPaginatorImpl} for Object paginator with AbstractMenu features}</p>
 */
public interface Paginator<T> {

  /**
   * Returns the item from the source index
   *
   * @param index int
   * @return T
   */
  T getItem(int index);

  /**
   * Checks if the Paginator contains the page in that index.
   * <p>If contains, it returns true</p>
   *
   * @param index int
   * @return Boolean
   */
  boolean hasPage(int index);

  /**
   * Returns the total pages of the Paginator
   *
   * @return int
   */
  int totalPages();

  /**
   * Checks if the paginator is in the first page
   *
   * @return boolean
   */
  boolean isFirst();

  /**
   * Check if the paginator is in the last page
   *
   * @return boolean
   */
  boolean isLast();

  /**
   * Goes to the first page of the paginator
   *
   * @return Paginator
   */
  Paginator<T> first();

  /**
   * Goes to the page provided of the paginator
   *
   * @return Paginator
   */
  Paginator<T> page(int index);

  /**
   * Goes to the previous page of the paginator
   */
  void previous();

  /**
   * Goes to the next page of the paginator
   */
  void next();

  /**
   * This can only be used for {@link MenuPaginatorImpl}, it creates the default previous and next
   * items in the menu
   */
  void setup(final Integer[] slots);

  /**
   * Gets the items contained on the page specified.
   *
   * @param index int
   * @return Set
   */
  Set<T> getItems(final int index);

}
