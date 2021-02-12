package com.celeste.menu.action;

import com.celeste.menu.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickAction {

    void run(MenuHolder holder, InventoryClickEvent event);

    default ClickAction and(ClickAction action) {
        return (menu, event) -> {
            run(menu, event);
            action.run(menu, event);
        };
    }

}
