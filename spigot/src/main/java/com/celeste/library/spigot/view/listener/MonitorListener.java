package com.celeste.library.spigot.view.listener;

import com.celeste.library.spigot.util.monitor.ChatMonitor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

import static com.celeste.library.spigot.util.monitor.ChatMonitor.*;

public final class MonitorListener implements Listener {

  private static boolean registered;

  static {
    registered = false;
  }

  public MonitorListener(final Plugin plugin) {
    if (!registered) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
      registered = true;
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
    final UUID id = event.getPlayer().getUniqueId();
    if (!MAP.contains(id)) {
      return;
    }

    event.setCancelled(true);

    final ChatMonitor monitor = MAP.remove(event.getPlayer().getUniqueId());
    final String message = event.getMessage();

    if (message.equalsIgnoreCase("cancelar") || message.equalsIgnoreCase("cancel")
        || message.contains("cancelar") || message.contains("cancel")) {
      monitor.getCancel().accept(null);
      return;
    }

    monitor.getMessage().accept(message);
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    MAP.remove(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onPlayerKick(final PlayerKickEvent event) {
    MAP.remove(event.getPlayer().getUniqueId());
  }

}