package com.celeste.library.spigot.model.menu.action;

import com.celeste.library.spigot.model.menu.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * The ClickAction receives two parameters of MenuHolder and InventoryClickEvent
 * Inside the lambda, you can execute whatever needed.
 */
public interface ClickAction {

    /**
     * Runs the ClickAction
     *
     * @param holder MenuHolder
     * @param event InventoryClickEvent
     */
    void run(MenuHolder holder, InventoryClickEvent event);

    /**
     * Lambda with the menu and event to execute whatever is needed by the developer.
     *
     * @param action ClickAction
     * @return ClickAction
     */
    default ClickAction and(ClickAction action) {
        return (menu, event) -> {
            run(menu, event);
            action.run(menu, event);
        };
    }

}
