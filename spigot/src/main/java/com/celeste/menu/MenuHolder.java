package com.celeste.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MenuHolder implements InventoryHolder {

    private final Menu menu;
    private final Map<String, Object> propertiesMap;

    @Setter
    private MenuItem[] items;

    private Inventory inventory;

    public MenuHolder(Menu menu, Map<String, Object> propertiesMap) {
        this.menu = menu;
        this.items = menu.getItems().clone();
        this.propertiesMap = new HashMap<>(propertiesMap);
    }

    /**
     * Puts the item on the specific slot.
     *
     * @param item ItemStack
     * @param slot Slot
     */
    public final MenuItem slot(int slot, ItemStack item) {
        final MenuItem menuItem = new MenuItem(slot).withItem(item);

        items[slot] = menuItem;

        return menuItem;
    }

    public final MenuItem slot(int slot) {
        final MenuItem menuItem = new MenuItem(slot);

        items[slot] = menuItem;

        return menuItem;
    }

    /**
     * Creates the inventory and sets all items.
     *
     * @param player Player that will open the inventory
     */
    public void show(Player player) {
        menu.onRender(player, this);

        final Inventory inventory = Bukkit.createInventory(this, menu.getSize(), menu.getTitle());

        for (MenuItem item : items) {
            if (item == null) continue;

            inventory.setItem(item.getSlot(), item.getItem());
        }

        player.openInventory(inventory);
    }

    protected void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);

        final int slot = event.getSlot();
        if(slot < 0) return;

        final MenuItem item = items[slot];
        if (item == null || item.getAction() == null) return;

        item.getAction().run(this, event);
    }

    protected final void handleOpen(InventoryOpenEvent event) {
        menu.onOpen(event, this);
    }

    protected final void handleClose(InventoryCloseEvent event) {
        menu.onClose(event, this);
    }

    /**
     * Gets the properties with that Key on the ImmutableMap.
     */
    public <T> T getProperty(String key) {
        return (T) propertiesMap.get(key);
    }

    public <T> T getProperty(String key, T defaultValue) {
        return (T) propertiesMap.getOrDefault(key, defaultValue);
    }

    public void setProperty(String key, Object value) {
        propertiesMap.put(key, value);
    }

    public boolean hasProperty(String key) {
        return propertiesMap.containsKey(key);
    }

}
