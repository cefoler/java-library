package com.celeste.library.spigot.model.paginator.impl;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.util.item.ItemBuilder;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class MenuPaginatorImpl<T> extends PaginatorImpl<T> {

  private final MenuHolder holder;

  public MenuPaginatorImpl(final MenuHolder holder, final Integer[] shape, final List<T> source) {
    super(shape, source);
    this.holder = holder;
  }

  /**
   * Setups the default items on the menu depending on the Source size and page.
   *
   * @param slots Integer of the slots that each item will be, first is for previous and second for
   *              next
   */
  public void setup(final Integer[] slots) {
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
    return new ItemBuilder(Material.ARROW)
        .name("§cPrevious page")
        .build();
  }

  private ItemStack getDefaultNextItem() {
    return new ItemBuilder(Material.ARROW)
        .name("§cNext page")
        .build();
  }

}
