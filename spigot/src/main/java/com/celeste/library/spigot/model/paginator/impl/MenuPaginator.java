package com.celeste.library.spigot.model.paginator.impl;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.util.ReflectionNms;
import com.celeste.library.spigot.util.item.ItemBuilder;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class MenuPaginator<T> extends PaginatorImpl<T> {

  private static final Material SKULL;

  static {
    SKULL = ReflectionNms.isEqualsOrMoreRecent(13)
        ? Enum.valueOf(Material.class, "PLAYER_HEAD")
        : Enum.valueOf(Material.class, "SKULL_ITEM");
  }

  private final MenuHolder holder;

  public MenuPaginator(final MenuHolder holder, final int[] shape, final List<T> source) {
    super(holder, shape, source);
    this.holder = holder;
  }

  /**
   * Setups the default items on the menu depending on the Source size and page.
   *
   * @param previous Previous page slot
   * @param next Next page slot
   */
  public void setup(final int previous, final int next) {
    if (!isFirst()) {
      holder.slot(previous, getDefaultPreviousItem())
          .action((holder, event) -> previous())
          .cancel()
          .update();
    }

    if (!isLast()) {
      holder.slot(next, getDefaultNextItem())
          .action((holder, event) -> next())
          .cancel()
          .update();
    }
  }

  private ItemStack getDefaultPreviousItem() {
    return new ItemBuilder(SKULL, 1, 3)
        .name("§cPrevious page")
        .skull("3162d42f4dba35488f4f66d673635bfc5619bdd513d02b4cc74f05ec8e956", UUID.randomUUID())
        .build();
  }

  private ItemStack getDefaultNextItem() {
    return new ItemBuilder(SKULL, 1, 3)
        .name("§cNext page")
        .skull("b813633bd60152d9df54b3d9d573a8bc36548b72dc1a30fb4cb9ec256d68ae", UUID.randomUUID())
        .build();
  }

}
