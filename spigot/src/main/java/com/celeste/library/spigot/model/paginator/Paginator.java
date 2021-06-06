package com.celeste.library.spigot.model.paginator;

import com.celeste.library.spigot.model.paginator.impl.MenuPaginator;
import com.celeste.library.spigot.model.paginator.impl.PaginatorImpl;
import java.util.List;

/**
 * The Paginator interface is the main creator for paginator.
 *
 * <p>Here you can access universal methods for
 * each of the paginator</p>
 *
 * <p>Type of paginator: {@link PaginatorImpl} for Object paginator and
 * {@link MenuPaginator} for Object paginator with AbstractMenu features}</p>
 */
public interface Paginator<T> {

  /**
   * Returns the total pages of the Paginator
   *
   * @return int
   */
  int totalPages();

  /**
   * Checks if the Paginator contains the page in that page.
   * <p>If contains, it returns true</p>
   *
   * @param page int
   * @return Boolean
   */
  boolean hasPage(final int page);

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
   * Goes to the last page of the paginator
   *
   * @return Paginator
   */
  Paginator<T> last();

  /**
   * Goes to the page provided of the paginator
   *
   * @return Paginator
   */
  Paginator<T> page(final int page);

  /**
   * Goes to the previous page of the paginator
   */
  Paginator<T> previous();

  /**
   * Goes to the next page of the paginator
   */
  Paginator<T> next();

  /**
   * Returns the item from the source index
   *
   * @param index int
   * @return T
   */
  T getItem(final int index);

  /**
   * Gets the items contained on the page specified.
   *
   * @param page int
   * @return Set
   */
  List<T> getItems(final int page);

}
