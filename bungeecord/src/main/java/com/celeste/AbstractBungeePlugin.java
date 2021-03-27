package com.celeste;

import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class AbstractBungeePlugin extends Plugin {

  private final PluginManager plugin;

  /**
   * Creates the AbstractBungeePlugin
   */
  public AbstractBungeePlugin() {
    this.plugin = getProxy().getPluginManager();
  }

  /**
   * Registers all listeners used as parameters
   *
   * @param listeners Listener classes
   */
  public void registerListeners(@NotNull final Listener... listeners) {
    Arrays.stream(listeners).forEach(listener -> plugin.registerListener(this, listener));
  }

  /**
   * Starts the BungeeFrame and MessageHolder With the default messages
   *
   * @param objects Command classes
   */
  public void startCommands(@NotNull final Object... objects) {
    final BungeeFrame frame = new BungeeFrame(this);
    final MessageHolder holder = frame.getMessageHolder();

    holder.setMessage(MessageType.ERROR, "§cA error occurred.");
    holder.setMessage(MessageType.INCORRECT_TARGET, "§cOnly players can execute this command..");
    holder.setMessage(MessageType.INCORRECT_USAGE, "§cWrong use! The correct is: /{usage}");
    holder.setMessage(MessageType.NO_PERMISSION, "§cYou don't have the permission to use this command!");

    frame.registerCommands(objects);
  }

}
