package com.celeste.library.spigot.model.menu;

import com.celeste.library.spigot.exception.InvalidPropertyException;
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

import java.util.Properties;

@Getter
public final class MenuHolder implements InventoryHolder {

    private Menu menu;
    private final Properties properties;

    private Inventory inventory;

    @Setter
    private MenuItem[] items;

    /**
     * Menu holder constructor.
     *
     * @param menu Menu
     * @param properties ImmutableMap of the properties.
     */
    public MenuHolder(final Menu menu, final Properties properties) {
        this.menu = menu;
        this.properties = properties;
        this.items = new MenuItem[menu.getSize()];
    }

    /**
     * Puts the item on the specific slot.
     *
     * @param item ItemStack
     * @param slot Slot
     *
     * @return MenuItem
     */
    public MenuItem slot(final int slot, final ItemStack item) {
        final MenuItem menuItem = new MenuItem(slot).item(item);

        items[slot] = menuItem;
        return menuItem;
    }

    /**
     * Reopens the Menu again with the new items
     * set in the holder
     */
    public void reopen() {
      inventory.clear();
      for (MenuItem item : getItems()) {
        if (item == null || item.getItem() == null) return;
        inventory.setItem(item.getSlot(), item.getItem());
      }
    }

    /**
     * Reopens the Menu again with the new items
     * and a new title set in the holder
     */
    public void reopen(final String title) {
      inventory.clear();
      for (MenuItem item : getItems()) {
        if (item == null || item.getItem() == null) return;
        inventory.setItem(item.getSlot(), item.getItem());
      }

      // TODO: Change via packets the title
    }

    /**
     * Reopens the Menu provided with the items,
     * title and slot without flicking (Via packets)
     */
    public void show(final Menu menu, final Player player) {
      inventory.clear();
      this.menu = menu;

      menu.onRender(player, this);
      for (MenuItem item : getItems()) {
        if (item == null || item.getItem() == null) return;
        inventory.setItem(item.getSlot(), item.getItem());
      }

      // TODO: Change via packets the title and size of the menu
    }

    /**
     * Creates the inventory and sets all items.
     *
     * @param player Player that will open the inventory
     */
    public void show(final Player player) {
        menu.onRender(player, this);
        this.inventory = Bukkit.createInventory(this, menu.getSize(), menu.getTitle());

        for (MenuItem item : items) {
          if (item == null) continue;
          inventory.setItem(item.getSlot(), item.getItem());
        }

        player.openInventory(inventory);
    }

    public void handleClick(final InventoryClickEvent event) {
        event.setCancelled(true);

        final int slot = event.getSlot();
        if (slot < 0) return;

        final MenuItem item = items[slot];
        if (item == null || item.getAction() == null) return;

        item.getAction().run(this, event);
    }

    public void handleOpen(final InventoryOpenEvent event) {
        menu.onOpen(event, this);
    }

    public void handleClose(final InventoryCloseEvent event) {
      menu.onClose(event, this);
    }

    /**
     * Gets the properties with that Key on the ImmutableMap.
     *
     * @param key Key to get the value
     * @param <T> Property class
     * @return Class of the property
     */
    @SuppressWarnings("unchecked")
    public <T> T getProperty(final String key) {
        if (properties.get(key) == null) throw new InvalidPropertyException("Get property returned null");
        return (T) properties.get(key);
    }

    /**
     * Sets the properties with that Key on the ImmutableMap.
     *
     * @param key Key for the value
     * @param value Property object
     */
    public void setProperty(final String key, final Object value) {
        properties.put(key, value);
    }

    /**
     * Checks it the property exists.
     *
     * @param key Key to get the value
     * @return boolean If exists
     */
    public boolean hasProperty(final String key) {
        return properties.containsKey(key);
    }

}
