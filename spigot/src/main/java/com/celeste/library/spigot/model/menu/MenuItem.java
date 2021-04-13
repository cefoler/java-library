package com.celeste.library.spigot.model.menu;

import com.celeste.library.spigot.model.menu.action.ClickAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Class for items of a Menu, that can execute
 * ClickActions and events.
 */
@Getter
@RequiredArgsConstructor
public final class MenuItem {

    private final int slot;

    private ItemStack item;
    private ClickAction action;

    /**
     * Sets the item of this MenuItem.
     * @param item ItemStack
     * @return MenuItem
     */
    public MenuItem item(final ItemStack item) {
        this.item = item;
        return this;
    }

    /**
     * Executes the ClickAction
     *
     * @param action ClickAction for the item.
     * @return MenuItem
     */
    public MenuItem action(final ClickAction action) {
        if (this.action == null) {
            this.action = action;
            return this;
        }

        this.action = this.action.and(action);
        return this;
    }

    /**
     * When the item is clicked, the menu is closed.
     *
     * @return MenuItem
     */
    public MenuItem close() {
        return action((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();
        });
    }

    /**
     * After the item is clicked, it sets a property into the holder.
     *
     * @param key Key
     * @param value Value for the key
     *
     * @return MenuItem
     */
    public MenuItem setProperty(final String key, final Object value) {
        return action((holder, event) -> holder.setProperty(key, value));
    }

    /**
     * When clicked, opens a new Menu without properties
     *
     * @param menu Menu that will be opened
     * @return MenuItem
     */
    public MenuItem open(final Menu menu) {
        return open(menu, new Properties());
    }

    /**
     * When clicked, opens a new Menu with properties
     *
     * @param menu Menu that will be opened
     * @param properties Immutable map of the properties.
     *
     * @return MenuItem
     */
    public MenuItem open(final Menu menu, final Properties properties) {
        return action((holder, event) -> {
            final Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();

            menu.show(whoClicked, properties);
        });
    }

    /**
     * When clicked, clears and updates the inventory
     * @return MenuItem
     */
    public MenuItem reopen() {
        return action((holder, event) -> {
            final ArrayList<MenuItem> items = holder.getInventory().getItems();

            holder.getInventory().getMenu().clear();
            holder.getInventory().setItems(items);
        });
    }

    public MenuItem cancel() {
      return action((holder, event) -> event.setCancelled(true));
    }

}
