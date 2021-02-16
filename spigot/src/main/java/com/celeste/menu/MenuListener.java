package com.celeste.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

/**
 * The listener for the Menu events.
 *
 * @author luiza
 */
public class MenuListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof MenuHolder)
            ((MenuHolder) (inventory.getHolder())).handleClick(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof MenuHolder)
            ((MenuHolder) (inventory.getHolder())).handleClose(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        final Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof MenuHolder)
            ((MenuHolder) (inventory.getHolder())).handleOpen(event);
    }

}
