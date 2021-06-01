package com.celeste.library.spigot.model.paginator.impl;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.util.item.ItemBuilder;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class MenuPaginator<T> extends PaginatorImpl<T> {

  private final MenuHolder holder;

  public MenuPaginator(final MenuHolder holder, final int[] shape, final List<T> source) {
    super(shape, source);
    this.holder = holder;
  }

  /**
   * Setups the default items on the menu depending on the Source size and page.
   *
   * @param slots Integer of the slots that each item will be,
   *              first is for previous and second for next
   */
  public void setup(final int[] slots) {
    if (getCurrentPage() > 0) {
      holder.slot(slots[0], getDefaultPreviousItem())
          .setProperty("page", getCurrentPage() - 1)
          .reopen();
    }

    if (getItems(getCurrentPage()).size() > (getCurrentPage() + 1) * getShape().length) {
      holder.slot(slots[1], getDefaultNextItem())
          .setProperty("page", getCurrentPage() + 1)
          .reopen();
    }
  }

  private ItemStack getDefaultPreviousItem() {
    return new ItemBuilder(Material.SKULL_ITEM, 1, 3)
        .name("§cPrevious page")
        .skull("3162d42f4dba35488f4f66d673635bfc5619bdd513d02b4cc74f05ec8e956", UUID.randomUUID())
        .build();
  }

  private ItemStack getDefaultNextItem() {
    return new ItemBuilder(Material.SKULL_ITEM, 1, 3)
        .name("§cNext page")
        .skull("b813633bd60152d9df54b3d9d573a8bc36548b72dc1a30fb4cb9ec256d68ae", UUID.randomUUID())
        .build();
  }

}
