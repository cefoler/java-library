package com.celeste.library.spigot.model.menu.entity.impl;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.model.menu.entity.AbstractContext;
import com.celeste.library.spigot.model.menu.entity.event.InventoryRenderEvent;
import org.bukkit.entity.Player;

public final class RenderContext extends AbstractContext<InventoryRenderEvent> {

  public RenderContext(Player player, InventoryRenderEvent context, MenuHolder holder) {
    super(player, context, holder);
  }

}
