package com.celeste.library.spigot.util.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class InventoryInjector {

  public static final InventoryInjector INSTANCE;

  static {
    INSTANCE = new InventoryInjector();
  }

  public List<ItemStack> getItemsFromMaterial(final Material material, final Player player) {
    return Arrays.stream(player.getInventory().getContents())
        .filter(Objects::nonNull)
        .filter(item -> item.getType() == material)
        .collect(Collectors.toList());
  }

  public void removeItemsFromMaterial(final Material material, final Player player) {
    Arrays.stream(player.getInventory().getContents())
        .filter(Objects::nonNull)
        .filter(item -> item.getType() == material)
        .forEach(item -> player.getInventory().remove(item));
  }

}
