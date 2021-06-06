package com.celeste.library.spigot.model.menu.entity;

import com.celeste.library.spigot.model.menu.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface Context<T extends Event> {

  T getContext();

  MenuHolder getHolder();

  Player getPlayer();

}
