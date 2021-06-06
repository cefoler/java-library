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
    return page >= 0 && page < totalPages();
  }

  @Override
  public boolean isFirst() {
    return currentPage == 0;
  }

  @Override
  public boolean isLast() {
    return currentPage == totalPages();
  }

  @Override
  public Paginator<T> first() {
    this.currentPage = 0;
    holder.setProperty("page", currentPage);
    return this;
  }

  @Override
  public Paginator<T> page(final int page) {
    this.currentPage = page;
    holder.setProperty("page", currentPage);
    return this;
  }

  @Override
  public Paginator<T> previous() {
    this.currentPage = currentPage - 1;
    holder.setProperty("page", currentPage);
    return this;
  }

  @Override
  public Paginator<T> next() {
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
    final int sourceSize = source.size();
    final int shapeLength = shape.length;

    final List<T> items = new ArrayList<>();

    System.out.println("A");
    if (sourceSize == 0) {
      return items;
    }

    System.out.println("B");
    if (sourceSize < shapeLength) {
      return new ArrayList<>(source);
    }

    System.out.println("C");
    if (page < 0 || page >= totalPages()) {
      throw new ArrayIndexOutOfBoundsException("The page must be more than 1 to a maximum of "
          + totalPages() + ", given: " + page);
    }

    System.out.println("D");
    for (int index = shapeLength * page; index < shapeLength; index++) {
      final T item = getItem(index);
      items.add(item);
    }

    System.out.println("E");
    return items;
  }

}
