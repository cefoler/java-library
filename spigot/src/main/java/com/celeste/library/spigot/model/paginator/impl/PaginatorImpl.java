package com.celeste.library.spigot.model.paginator.impl;

import com.celeste.library.spigot.exception.MenuException;
import com.celeste.library.spigot.model.paginator.Paginator;
import com.celeste.library.spigot.model.paginator.PaginatorContent;
import lombok.Getter;

import java.util.*;

@Getter
public class PaginatorImpl<T> extends PaginatorContent<T> implements Paginator<T> {

  private int currentPage;

  public PaginatorImpl(final Integer[] shape, final List<T> source) {
    super(shape, source);
    this.currentPage = 1;
  }

  /**
   * Returns the resource on that index
   * @param index int
   *
   * @return T
   */
  @Override
  public T getItem(final int index) {
    return getSource().get(index);
  }

  /**
   * Checks if it contains that page.
   *
   * @param index int
   * @return boolean If exists
   */
  @Override
  public boolean hasPage(final int index) {
    return index >= 0 && index < totalPages();
  }

  /**
   * Returns the number of pages that his paginator
   * contains depending on his shape and source size.
   *
   * @return int
   */
  @Override
  public int totalPages() {
    return (int) Math.ceil((double) getSource().size() / getShape().length);
  }

  /**
   * Gets all items registered on that
   * page in the Paginator
   *
   * @param page Integer
   * @return List
   */
  @Override
  public Set<T> getItems(final int page) {
    final int sourceSize = getSource().size();
    final int shapeLength = getShape().length;

    final Set<T> items = new LinkedHashSet<>();

    if (sourceSize == 0) return items;
    if (sourceSize < shapeLength) return new LinkedHashSet<>(getSource());
    if (page < 0 || page >= totalPages()) {
      throw new MenuException("The page index must be more than 1 to a maximum of " + totalPages() + ", given: " + page);
    }

    for (int i = shapeLength * page; i < shapeLength; i++) {
      items.add(getSource().get(i));
    }

    return items;
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
    return this;
  }

  @Override
  public Paginator<T> page(int index) {
    this.currentPage = index;
    return this;
  }

  @Override
  public void previous() {
    this.currentPage = currentPage - 1;
  }

  @Override
  public void next() {
    this.currentPage = currentPage + 1;
  }

  @Override
  public void setup(final Integer[] slots) {
    // do nothing, this isn't implemented on PaginatorImpl.
  }

}
