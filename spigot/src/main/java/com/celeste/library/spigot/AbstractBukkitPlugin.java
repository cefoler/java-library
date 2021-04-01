package com.celeste.library.spigot;

import com.celeste.library.spigot.annotation.CommandHolder;
import com.celeste.library.spigot.exception.InvalidCommandException;
import com.celeste.library.spigot.exception.InvalidListenerException;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static me.saiintbrisson.minecraft.command.message.MessageType.*;

@Getter
public abstract class AbstractBukkitPlugin extends JavaPlugin {

  private final PluginManager manager;
  private final ServicesManager service;

  public AbstractBukkitPlugin() {
    this.manager = Bukkit.getServer().getPluginManager();
    this.service = Bukkit.getServer().getServicesManager();
  }

  public <T extends AbstractBukkitPlugin> void registerListeners(@NotNull final Class<T> plugin, @NotNull final T instance) {
    try {
      for (final Class<? extends Listener> clazz : new Reflections("").getSubTypesOf(Listener.class)) {
        final Constructor<? extends Listener> constructor = (Constructor<? extends Listener>) clazz.getConstructors()[0];

        final Listener listener = constructor.getParameterCount() != 0
            ? Arrays.asList(constructor.getParameterTypes()).contains(plugin)
            ? constructor.newInstance(instance)
            : null
            : constructor.newInstance();

        if (listener == null) continue;

        manager.registerEvents(listener, instance);
      }
    } catch (Throwable throwable) {
      throw new InvalidListenerException("Unable to register listener: ", throwable);
    }

  }

  /**
   * Starts the BukkitFrame and MessageHolder
   * With the default messages
   */
  public <T extends AbstractBukkitPlugin> void registerCommands(@NotNull final Class<T> plugin, @NotNull final T instance) {
    try {
      final BukkitFrame frame = new BukkitFrame(this);
      final MessageHolder holder = frame.getMessageHolder();

      holder.setMessage(ERROR, "§cA error occurred.");
      holder.setMessage(INCORRECT_TARGET, "§cOnly players can execute this command..");
      holder.setMessage(INCORRECT_USAGE, "§cWrong use! The correct is: /{usage}");
      holder.setMessage(NO_PERMISSION, "§cYou don't have enough permissions.");

      for (final Class<?> clazz : new Reflections("").getTypesAnnotatedWith(CommandHolder.class)) {
        final Constructor<?> constructor = clazz.getConstructors()[0];

        final Object command = constructor.getParameterCount() != 0
            ? Arrays.asList(constructor.getParameterTypes()).contains(plugin)
            ? constructor.newInstance(instance)
            : null
            : constructor.newInstance();

        if (command == null) continue;

        frame.registerCommands(command);
      }
    } catch (Throwable throwable) {
      throw new InvalidCommandException("Unable to register command: ", throwable);
    }
  }

}
