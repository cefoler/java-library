package com.celeste.library.spigot.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
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

  @Deprecated
  public List<ItemStack> getItemsFromOldData(final Material material, int data, final Player player) {
    return Arrays.stream(player.getInventory().getContents())
        .filter(Objects::nonNull)
        .filter(item -> item.getType() == material)
        .filter(item -> item.getDurability() == data)
        .collect(Collectors.toList());
  }

  public int removeItemsFromMaterial(final Material material, final Player player) {
    final AtomicInteger count = new AtomicInteger();
    Arrays.stream(player.getInventory().getContents())
        .filter(Objects::nonNull)
        .filter(item -> item.getType() == material)
        .forEach(item -> {
          count.set(count.get() + item.getAmount());
          player.getInventory().remove(item);
        });

    return count.get();
  }

  public void removeItemsFromMaterial(final Material material, int amount, final Player player) {
    final AtomicInteger count = new AtomicInteger();
    final List<ItemStack> items = Arrays.stream(player.getInventory().getContents())
        .filter(Objects::nonNull)
        .filter(item -> item.getType() == material)
        .collect(Collectors.toList());

    for (ItemStack item : items) {
      final int newCount = count.get();
      if (newCount + item.getAmount() > amount) {
        final int amountLeft = newCount - amount;
        item.setAmount(amountLeft);

        player.getInventory().remove(item);
        return;
      }

      count.set(newCount + item.getAmount());
      player.getInventory().remove(item);
    }
  }

}
