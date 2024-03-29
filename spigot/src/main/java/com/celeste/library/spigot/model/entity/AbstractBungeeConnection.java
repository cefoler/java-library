package com.celeste.library.spigot.model.entity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * Creates a connection between the Bukkit plugin with the Bungeecord.
 *
 * <p>This factory auxiliates
 * to send the player to another servers connected into the Bungeecord
 *
 * @param <T> AbstractBukkitPlugin
 */
@Getter
public abstract class AbstractBungeeConnection<T extends Plugin>
    implements PluginMessageListener {

  private final T plugin;
  private final Messenger messenger;

  private boolean connected;

  private static final String CHANNEL;

  static {
    CHANNEL = "BungeeCord";
  }

  public AbstractBungeeConnection(final T plugin) {
    this.plugin = plugin;
    this.messenger = plugin.getServer().getMessenger();
    this.connected = false;

    register();
  }

  public void register() {
    messenger.registerIncomingPluginChannel(plugin, CHANNEL, this);
    messenger.registerOutgoingPluginChannel(plugin, CHANNEL);

    this.connected = true;
  }

  public void unregister() {
    messenger.unregisterIncomingPluginChannel(plugin, CHANNEL, this);
    messenger.unregisterOutgoingPluginChannel(plugin, CHANNEL);

    this.connected = false;
  }

  /**
   * Connects the player to the following server
   *
   * @param server String
   * @param player String
   */
  @SneakyThrows
  public void connect(final String server, final Player player) {
    try (
        final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        final DataOutputStream dateOutput = new DataOutputStream(byteOutput)
    ) {

      dateOutput.writeUTF("Connect");
      dateOutput.writeUTF(server);

      player.sendPluginMessage(plugin, CHANNEL, byteOutput.toByteArray());
    } catch (IOException exception) {
      throw new UnsupportedOperationException("Unable to connect server: " + server
          + " through Bungeecord", exception);
    }
  }

  @Override
  public void onPluginMessageReceived(final String message, final Player player, final byte[] bytes) {}

}
