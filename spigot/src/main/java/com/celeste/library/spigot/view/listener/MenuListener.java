package com.celeste.library.spigot.view.listener;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.view.event.wrapper.impl.InventoryRenderEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public final class MenuListener implements Listener {

  private static boolean register;

  static {
    register = false;
  }

  public MenuListener(final Plugin plugin) {
    if (!register) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
      register = true;
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClick(final InventoryClickEvent event) {
    final Inventory inventory = event.getInventory();
    if (inventory.getHolder() instanceof MenuHolder) {
      final MenuHolder holder = (MenuHolder) inventory.getHolder();
      holder.handleClick(event);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryRender(final InventoryRenderEvent event) {
    final Inventory inventory = event.getInventory();
    if (inventory.getHolder() instanceof MenuHolder) {
      final MenuHolder holder = (MenuHolder) inventory.getHolder();
      holder.handleRender(event);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryOpen(final InventoryOpenEvent event) {
    final Inventory inventory = event.getInventory();
    if (inventory.getHolder() instanceof MenuHolder) {
      final MenuHolder holder = (MenuHolder) inventory.getHolder();
      holder.handleOpen(event);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClose(final InventoryCloseEvent event) {
    final Inventory inventory = event.getInventory();
    if (inventory.getHolder() instanceof MenuHolder) {
      final MenuHolder holder = (MenuHolder) inventory.getHolder();
      holder.handleClose(event);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryDrag(final InventoryDragEvent event) {
    final Inventory inventory = event.getInventory();
    if (inventory.getHolder() instanceof MenuHolder) {
      final MenuHolder holder = (MenuHolder) inventory.getHolder();
      holder.handleDrag(event);
    }
  }

}
