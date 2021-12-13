package com.celeste.library.spigot.view.listener;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.view.event.wrapper.impl.InventoryRenderEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public final class MenuListener implements Listener {

  private static boolean REGISTER;

  static {
    REGISTER = false;
  }

  public MenuListener(final Plugin plugin) {
    if (!REGISTER) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
      REGISTER = true;
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClick(final InventoryClickEvent event) {
    final MenuHolder holder = getHolder(event);
    if (holder == null) {
      return;
    }

    holder.handleClick(event);
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryRender(final InventoryRenderEvent event) {
    final MenuHolder holder = getHolder(event);
    if (holder == null) {
      return;
    }

    holder.handleRender(event);
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryOpen(final InventoryOpenEvent event) {
    final MenuHolder holder = getHolder(event);
    if (holder == null) {
      return;
    }

    holder.handleOpen(event);
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClose(final InventoryCloseEvent event) {
    final MenuHolder holder = getHolder(event);
    if (holder == null) {
      return;
    }

    holder.handleClose(event);
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryDrag(final InventoryDragEvent event) {
    final MenuHolder holder = getHolder(event);
    if (holder == null) {
      return;
    }

    holder.handleDrag(event);
  }

  private MenuHolder getHolder(final InventoryEvent event) {
    final Inventory inventory = event.getInventory();
    if (inventory.getHolder() instanceof MenuHolder) {
      return (MenuHolder) inventory.getHolder();
    }

    return null;
  }

}
