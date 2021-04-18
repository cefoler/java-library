package com.celeste.library.spigot.model;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.util.item.ItemBuilder;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Paginator creates a paged menu with
 * the Object T, size and object source provided.
 *
 * @param <T> Object
 */
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
     * Gets all items registered on that
     * page in the Paginator
     *
     * @param page Integer
     * @return List
     */
    public List<T> getItems(final int page) {
        int size = size();

        if (source.isEmpty()) return Collections.emptyList();
        if (size < pageSize) return Lists.newArrayList(source);
        if (page < 0 || page >= count()) {
            throw new ArrayIndexOutOfBoundsException("Index must be between the range of 0 and " + (count() - 1) + ", given: " + page);
        }

        final List<T> pageItems = new LinkedList<>();

        int base = page * pageSize;
        int until = base + pageSize;

        if (until > size()) until = size;

        for (int i = base; i < until; i++) {
            pageItems.add(get(i));
        }

        return pageItems;
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

    public ItemStack getDefaultPreviousItem() {
        return new ItemBuilder(Material.ARROW)
                .name("§cPrevious page")
                .build();
    }

    public ItemStack getDefaultNextItem() {
        return new ItemBuilder(Material.ARROW)
                .name("§cNext page")
                .build();
    }

    /**
     * Setups the default items on the menu depending
     * on the Source size and page.
     *
     * @param holder MenuHolder
     * @param page int
     * @param shape Integer[]
     */
    public void setupDefault(final MenuHolder holder, int page, final Integer[] shape, final Integer[] slots) {
      final List<T> source = getItems(page);
      if (page > 0) {
        holder.slot(slots[0], getDefaultPreviousItem())
            .setProperty("page", page - 1)
            .reopen();
      }

      if (source.size() > (page + 1) * shape.length) {
        holder.slot(slots[1], getDefaultNextItem())
            .setProperty("page", page + 1)
            .reopen();
      }
    }

}
