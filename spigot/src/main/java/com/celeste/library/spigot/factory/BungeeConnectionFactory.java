package com.celeste.library.spigot.factory;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Creates a connection between the Bukkit plugin
 * with the Bungeecord.
 *
 * <p>This factory auxiliates
 * to send the player to another servers
 * connected into the Bungeecord
 *
 * @param <T> AbstractBukkitPlugin
 */
@Getter
public final class BungeeConnectionFactory<T extends Plugin> implements PluginMessageListener {

  private final T plugin;
  private final Messenger messenger;

  private boolean connected;

  /**
   * Creates the instance of the factory
   * @param plugin LobbyPlugin
   */
  public BungeeConnectionFactory(final T plugin) {
    this.plugin = plugin;
    this.messenger = plugin.getServer().getMessenger();
    this.connected = false;
  }

  /**
   * Loads the Bungeecord channel
   */
  public void load() {
    messenger.registerIncomingPluginChannel(plugin, "BungeeCord", this);
    messenger.registerOutgoingPluginChannel(plugin, "BungeeCord");
    this.connected = true;
  }

  /**
   * Unloads the Bungeecord channel
   */
  public void unload() {
    messenger.unregisterIncomingPluginChannel(plugin, "BungeeCord", this);
    messenger.unregisterOutgoingPluginChannel(plugin, "BungeeCord");
    this.connected = false;
  }

  /**
   * Connects the player to the following server
   * @param server String
   * @param player String
   */
  @SneakyThrows
  public void connect(final String server, final Player player) {
    try (
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DataOutputStream dateOutput = new DataOutputStream(byteOutput)
    ) {

      dateOutput.writeUTF("Connect");
      dateOutput.writeUTF(server);

      player.sendPluginMessage(plugin, "BungeeCord", byteOutput.toByteArray());
    } catch (IOException exception) {
      throw new UnsupportedOperationException("Unable to connect server: " + server + " through Bungeecord", exception);
    }
  }

  @Override
  public void onPluginMessageReceived(String s, Player player, byte[] bytes) {}

}
