package com.celeste.library.spigot.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public final class Paginator<T> {

    @Setter
    private int pageSize;

    private final List<T> source;

    /**
     * @return int Page size
     */
    public int size() {
        return source.size();
    }

    /**
     * Gets item on that index.
     *
     * @param index int
     * @return Item object
     */
    public T get(final int index) {
        return source.get(index);
    }

    /**
     * @return Int with size of the page
     */
    public int count() {
        return (int) Math.ceil((double) size() / pageSize);
    }

    /**
     * Gets page on that index.
     *
     * @param index int
     * @return List
     */
    public List<T> getPage(final int index) {
        int size = size();

        if (source.isEmpty()) return Collections.emptyList();
        if (size < pageSize) return Lists.newArrayList(source);
        if (index < 0 || index >= count()) {
            throw new ArrayIndexOutOfBoundsException(
                "Index must be between the range of 0 and " + (count() - 1) + ", given: " + index
            );
        }

        final List<T> page = new LinkedList<>();

        int base = index * pageSize;
        int until = base + pageSize;

        if (until > size()) until = size;

        for (int i = base; i < until; i++) {
            page.add(get(i));
        }

        return page;
    }

    /**
     * Checks if it contains that page.
     *
     * @param index int
     * @return boolean If exists
     */
    public boolean hasPage(final int index) {
        return index >= 0 && index < count();
    }

}
