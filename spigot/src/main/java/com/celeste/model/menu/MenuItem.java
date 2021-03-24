package com.celeste.model.menu;

import com.celeste.model.menu.action.ClickAction;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Class for items of a Menu, that can execute
 * ClickActions and events.
 *
 * @author luiza
 */
@Getter
@RequiredArgsConstructor
public class MenuItem {

    private final int slot;

    private ItemStack item;
    private ClickAction action;

    /**
     * Sets the item of this MenuItem.
     * @param item ItemStack
     * @return MenuItem
     */
    public MenuItem withItem(final ItemStack item) {
        this.item = item;
        return this;
    }

    /**
     * Executes the ClickAction
     *
     * @param action ClickAction for the item.
     * @return MenuItem
     */
    public MenuItem withAction(final ClickAction action) {
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
        return withAction((holder, event) -> {
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
        return withAction((holder, event) -> holder.setProperty(key, value));
    }

    /**
     * When clicked, opens a new Menu without properties
     *
     * @param menu Menu that will be opened
     * @return MenuItem
     */
    public MenuItem open(final Menu menu) {
        return open(menu, ImmutableMap.of());
    }

    /**
     * When clicked, opens a new Menu with properties
     *
     * @param menu Menu that will be opened
     * @param properties Immutable map of the properties.
     *
     * @return MenuItem
     */
    public MenuItem open(final Menu menu, final Map<String, Object> properties) {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();

            menu.show(whoClicked, properties);
        });
    }

    /**
     * When clicked, reopens the same Menu.
     *
     * @return MenuItem
     */
    public MenuItem reopen() {
        return withAction((holder, event) -> {
            final Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();

            holder.setItems(holder.getMenu().getItems().clone());
            holder.show(whoClicked);
        });
    }

}
