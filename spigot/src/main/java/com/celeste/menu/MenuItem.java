package com.celeste.menu;

import com.celeste.menu.action.ClickAction;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
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

    public MenuItem withItem(ItemStack item) {
        this.item = item;
        return this;
    }

    /**
     * Executes the ClickAction
     *
     * @param action ClickAction for the item.
     */
    public MenuItem withAction(ClickAction action) {
        if (this.action == null) {
            this.action = action;
        } else {
            this.action = this.action.and(action);
        }

        return this;
    }

    /**
     * When the item is clicked, a sound is played.
     *
     * @param sound Sound
     */
    public MenuItem soundOnClick(Sound sound, float v, float v1) {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.playSound(whoClicked.getLocation(), sound, v, v1);
        });
    }

    /**
     * When the item is clicked, messages are sent to the player.
     *
     * @param messages Message that will be sent
     */
    public MenuItem messagesOnClick(String... messages) {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.sendMessage(messages);
        });
    }

    /**
     * When the item is clicked, the menu is closed.
     */
    public MenuItem closeOnClick() {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();
        });
    }

    public MenuItem setPropertyOnClick(String key, Object value) {
        return withAction((holder, event) -> {
            holder.setProperty(key, value);
        });
    }

    /**
     * When clicked, opens a new Menu without properties
     *
     * @param menu Menu that will be opened
     */
    public MenuItem openMenuOnClick(Menu menu) {
        return openMenuOnClick(menu, ImmutableMap.of());
    }

    /**
     * When clicked, opens a new Menu with properties
     *
     * @param menu Menu that will be opened
     * @param properties Immutable map of the properties.
     */
    public MenuItem openMenuOnClick(Menu menu, Map<String, Object> properties) {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();

            menu.show(whoClicked, properties);
        });
    }

    /**
     * When clicked, reopens the same Menu.
     */
    public MenuItem reopenOnClick() {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();

            holder.setItems(holder.getMenu().getItems().clone());
            holder.show(whoClicked);
        });
    }

}
