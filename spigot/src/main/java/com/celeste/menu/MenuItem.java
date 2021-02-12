package com.celeste.menu;


import com.celeste.menu.action.ClickAction;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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

    public MenuItem withAction(ClickAction action) {
        if(this.action == null) {
            this.action = action;
        } else {
            this.action = this.action.and(action);
        }

        return this;
    }

    public MenuItem soundOnClick(Sound sound, float v, float v1) {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.playSound(whoClicked.getLocation(), sound, v, v1);
        });
    }

    public MenuItem messagesOnClick(String... messages) {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.sendMessage(messages);
        });
    }

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

    public MenuItem openMenuOnClick(Menu menu) {
        return openMenuOnClick(menu, ImmutableMap.of());
    }

    public MenuItem openMenuOnClick(Menu menu, Map<String, Object> properties) {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();

            menu.show(whoClicked, properties);
        });
    }

    public MenuItem reopenOnClick() {
        return withAction((holder, event) -> {
            Player whoClicked = (Player) event.getWhoClicked();
            whoClicked.closeInventory();

            holder.setItems(holder.getMenu().getItems().clone());
            holder.show(whoClicked);
        });
    }

}
