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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static me.saiintbrisson.minecraft.command.message.MessageType.*;

/**
 * The AbstractBukkitPlugin is the main implementation
 * of a JavaPlugin, it automatically registers listeners
 * and commands made by the command-framework
 */
@Getter
public abstract class AbstractBukkitPlugin extends JavaPlugin {

  private final PluginManager manager;
  private final ServicesManager service;

  private final ExecutorService executor;
  private final ScheduledExecutorService scheduledExecutor;

  public AbstractBukkitPlugin() {
    getDataFolder().mkdirs();

    this.manager = Bukkit.getServer().getPluginManager();
    this.service = Bukkit.getServer().getServicesManager();

    this.executor = Executors.newCachedThreadPool();
    this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
  }

  /**
   * This method registers the listeners and commands in a unique method.
   * <p>Basically just remove one line of code</p>
   * @param plugin Class of the plugin
   * @param instance T
   * @param <T> Instance of your plugin class
   */
  public <T extends AbstractBukkitPlugin> void register(@NotNull final Class<T> plugin, @NotNull final T instance) {
    registerListeners(plugin, instance);
    registerCommands(plugin, instance);
  }

  /**
   * Registers all listeners that implements the {@link Listener}
   * @param plugin Class of the plugin
   * @param instance T
   * @param <T> Instance of your plugin class
   */
  @SuppressWarnings("unchecked")
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
   * Registers all commands that contains a {@link CommandHolder}
   * annotation at the top of the class.
   *
   * @param plugin Class of the plugin
   * @param instance T
   * @param <T> Instance of your plugin class
   */
  public <T extends AbstractBukkitPlugin> void registerCommands(@NotNull final Class<T> plugin, @NotNull final T instance) {
    try {
      final BukkitFrame frame = new BukkitFrame(this);
      final MessageHolder holder = frame.getMessageHolder();

      holder.setMessage(ERROR, "§cA error occurred.");
      holder.setMessage(INCORRECT_TARGET, "§cOnly {target} can execute this command..");
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
