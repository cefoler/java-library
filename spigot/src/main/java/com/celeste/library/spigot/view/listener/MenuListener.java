package com.celeste.library.spigot.view.listener;

import com.celeste.library.spigot.model.menu.MenuHolder;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

@NoArgsConstructor
public final class MenuListener implements Listener {

    /**
     * Registers menu InventoryClick event.
     *
     * @param event InventoryClickEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof MenuHolder) {
            ((MenuHolder) (inventory.getHolder())).handleClick(event);
        }
    }

    /**
     * Registers menu InventoryClick event.
     *
     * @param event InventoryClickEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(final InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof MenuHolder)) {
            event.setCancelled(true);
        }
    }

    /**
     * Registers menu InventoryCloseEvent event.
     *
     * @param event InventoryCloseEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof MenuHolder) {
            ((MenuHolder) (inventory.getHolder())).handleClose(event);
        }
    }

    /**
     * Registers menu InventoryOpenEvent event.
     *
     * @param event InventoryOpenEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(final InventoryOpenEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof MenuHolder) {
            ((MenuHolder) (inventory.getHolder())).handleOpen(event);
        }
    }

}
