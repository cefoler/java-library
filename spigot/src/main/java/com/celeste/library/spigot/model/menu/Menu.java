package com.celeste.library.spigot.model.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Properties;

/**
 * The Menu class should be extended on the class that
 * will create the Menu
 *
 * When extended, the constructor will create a super with Title and Size
 * For modifications on the inventory, you should create the onRender,
 * onOpen and onClose on the class extended by Menu.
 */
@Getter
public abstract class Menu {

    private final String title;
    private final int size;

    private final ArrayList<MenuItem> items;

    /**
     * Creates the Menu with the title and size specified.
     *
     * @param title String
     * @param size int
     */
    public Menu(final String title, final int size) {
        this.title = title;
        this.size = size;
        this.items = new ArrayList<>(size);
    }

    /**
     * Sets the item on the specific slot.
     *
     * @param slot Int
     * @param item ItemStack
     *
     * @return MenuItem
     */
    public final MenuItem slot(final int slot, final ItemStack item) {
        final MenuItem menuItem = new MenuItem(slot).item(item);
        items.set(slot, menuItem);

        return menuItem;
    }

    /**
     * Event when the menu is rendered
     *
     * @param player Player that will render the menu
     * @param holder MenuHolder
     */
    public abstract void onRender(final Player player, final MenuHolder holder);

    /**
     * Event when the menu is opened
     *
     * @param event InventoryOpenEvent for the Menu
     * @param holder MenuHolder
     */
    public abstract void onOpen(final InventoryOpenEvent event, final MenuHolder holder);

    /**
     * Event when the menu is opened
     *
     * @param event InventoryCloseEvent for the Menu
     * @param holder MenuHolder
     */
    public abstract void onClose(final InventoryCloseEvent event, final MenuHolder holder);

    /**
     * Shows the player the Menu, this method doesn't contains
     * Any properties.
     *
     * @param player The player that will open the Menu
     * @return MenuHolder
     */
    public final MenuHolder show(final Player player) {
        return show(player, new Properties());
    }

    /**
     * Shows the player the Menu.
     *
     * @param player The player that will open the Menu
     * @param properties ImmutableMap of properties needed on the Menu class
     *
     * @return MenuHolder
     */
    public final MenuHolder show(final Player player, final Properties properties) {
        final MenuHolder holder = new MenuHolder(this, properties);
        holder.show(player);

        return holder;
    }

}

