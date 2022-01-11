package com.celeste.library.spigot.util.injector;

import com.celeste.library.spigot.util.item.ItemBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class InventoryInjector {

  public static final InventoryInjector INSTANCE;

  static {
    INSTANCE = new InventoryInjector();
  }

  public List<ItemStack> getItemsFromMaterial(final Material material, final Player player) {
    return Arrays.stream(player.getInventory().getContents())
        .filter(item -> item != null && item.getType() == material)
        .collect(Collectors.toList());
  }

  @Deprecated
  public List<ItemStack> getItemsFromOldData(final Material material, int data,
      final Player player) {
    return Arrays.stream(player.getInventory().getContents())
        .filter(item -> item != null && item.getType() == material && item.getDurability() == data)
        .collect(Collectors.toList());
  }

  public List<ItemStack> getItemsFromModelData(final Material material, int data, final Player player) {
    return Arrays.stream(player.getInventory().getContents())
        .filter(item -> {
          if (item == null || item.getType() != material) {
            return false;
          }

          final ItemBuilder builder = new ItemBuilder(item);
          return builder.hasModelData(data);
        })
        .collect(Collectors.toList());
  }

  public int removeItemsFromMaterial(final Material material, final Player player) {
    final AtomicInteger count = new AtomicInteger();
    Arrays.stream(player.getInventory().getContents())
        .filter(item -> item != null && item.getType() == material)
        .forEach(item -> {
          count.set(count.get() + item.getAmount());
          player.getInventory().remove(item);
        });

    return count.get();
  }

  public void removeItemsFromMaterial(final Material material, int amount, final Player player) {
    final List<ItemStack> items = Arrays.stream(player.getInventory().getContents())
        .filter(item -> item != null && item.getType() == material)
        .collect(Collectors.toList());

    final AtomicInteger count = new AtomicInteger();
    items.forEach(item -> {
      final int newCount = count.get();
      if (newCount + item.getAmount() < amount) {
        count.set(newCount + item.getAmount());

        player.getInventory().remove(item);
        return;
      }

      final int amountLeft = newCount - amount;
      item.setAmount(amountLeft);

      player.getInventory().remove(item);
    });
  }

}
