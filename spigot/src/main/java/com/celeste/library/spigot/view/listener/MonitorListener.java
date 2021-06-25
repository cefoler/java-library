package com.celeste.library.spigot.view.listener;

import com.celeste.library.spigot.util.monitor.ChatMonitor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

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
    final ChatMonitor monitor = ChatMonitor.MAP.remove(event.getPlayer().getUniqueId());
    if (monitor == null) {
      return;
    }

    event.setCancelled(true);
    event.setMessage(null);

    final String message = event.getMessage();

    // Starts the cancel consumer
    if (message.equalsIgnoreCase("cancelar") || message.equalsIgnoreCase("cancel") || message.contains("cancelar") || message.contains("cancel")) {
      monitor.getCancel().accept(null);
      return;
    }

    monitor.getMessage().accept(message);
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    final Player player = event.getPlayer();
    ChatMonitor.MAP.remove(player.getUniqueId());
  }

  @EventHandler
  public void onPlayerKick(final PlayerKickEvent event) {
    final Player player = event.getPlayer();
    ChatMonitor.MAP.remove(player.getUniqueId());
  }

}