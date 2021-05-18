package com.celeste.library.spigot.model.menu.action;

import com.celeste.library.spigot.model.menu.MenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * The ClickAction is executed when the MenuItem with this action is clicked.
 *
 * <p>While creating the ClickAction, it provides the
 * holder and the InventoryClickEvent for the player to use as needed</p>
 */
public interface ClickAction {

  /**
   * Runs the ClickAction
   *
   * @param holder MenuHolder
   * @param event  InventoryClickEvent
   */
  void run(final MenuHolder holder, final InventoryClickEvent event);

  /**
   * Lambda with the menu and event to execute whatever is needed by the developer.
   *
   * @param action ClickAction
   * @return ClickAction
   */
  default ClickAction and(final ClickAction action) {
    return (menu, event) -> {
      run(menu, event);
      action.run(menu, event);
    };
  }

}
