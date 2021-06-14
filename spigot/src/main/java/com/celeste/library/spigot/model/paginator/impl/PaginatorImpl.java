package com.celeste.library.spigot.model.paginator.impl;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.model.paginator.AbstractPaginator;
import com.celeste.library.spigot.model.paginator.Paginator;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PaginatorImpl<T> extends AbstractPaginator<T> {

  private int currentPage;

  public PaginatorImpl(final MenuHolder holder, final int[] shape, final List<T> source) {
    super(holder, shape, source);
    this.currentPage = holder.getProperty("page");
  }

  /**
   * Returns the number of pages that his paginator contains depending on his shape and source
   * size.
   *
   * @return int
   */
  @Override
  public int totalPages() {
    return (int) Math.ceil((double) source.size() / shape.length);
  }

  /**
   * Checks if it contains that page.
   *
   * @param page int
   * @return boolean If exists
   */
  @Override
  public boolean hasPage(final int page) {
    return page >= 0 && page <= totalPages();
  }

  @Override
  public boolean isFirst() {
    return currentPage == 0;
  }

  @Override
  public boolean isLast() {
    return currentPage == totalPages() - 1;
  }

  @Override
  public Paginator<T> first() {
    this.currentPage = 0;
    holder.setProperty("page", currentPage);
    return this;
  }

  @Override
  public Paginator<T> last() {
    this.currentPage = totalPages();
    holder.setProperty("page", currentPage);
    return this;
  }

  @Override
  public Paginator<T> page(final int page) {
    this.currentPage = page < 0 ? 0 : page > totalPages() ? totalPages() : page;
    holder.setProperty("page", currentPage);
    return this;
  }

  @Override
  public Paginator<T> previous() {
    if (isFirst()) {
      return this;
    }

    this.currentPage = currentPage - 1;
    holder.setProperty("page", currentPage);
    return this;
  }

  @Override
  public Paginator<T> next() {
    if (isLast()) {
      return this;
    }

    this.currentPage = currentPage + 1;
    holder.setProperty("page", currentPage);
    return this;
  }

  /**
   * Returns the resource on that index
   *
   * @param index int
   * @return T
   */
  @Override
  public T getItem(final int index) {
    return source.get(index);
  }

  /**
   * Gets all items registered on that page in the Paginator
   *
   * @param page Integer
   * @return List
   */
  @Override
  public List<T> getItems(final int page) {
    final List<T> items = new ArrayList<>();

    if (source.isEmpty()) {
      return items;
    }

    if (source.size() < shape.length) {
      return new ArrayList<>(source);
    }

    if (page < 0 || page > totalPages()) {
      throw new ArrayIndexOutOfBoundsException("The page must be more than 1 to a maximum of "
          + totalPages() + ", given: " + page);
    }

    final int base = shape.length * (page - 1);
    final int max = Math.min(base + shape.length, source.size());

    for (int index = base; index < max; index++) {
      final T item = getItem(index);
      items.add(item);
    }

    return items;
  }

}
