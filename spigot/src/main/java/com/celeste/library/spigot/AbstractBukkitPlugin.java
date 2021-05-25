package com.celeste.library.spigot;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.annotation.CommandHolder;
import com.celeste.library.spigot.exception.InvalidCommandException;
import com.celeste.library.spigot.exception.InvalidListenerException;
import java.lang.reflect.Constructor;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

@Getter
public abstract class AbstractBukkitPlugin extends JavaPlugin {

  private final PluginManager manager;
  private final ServicesManager service;

  private final ExecutorService executor;
  private final ScheduledExecutorService scheduledExecutor;

  public AbstractBukkitPlugin() {
    this.manager = Bukkit.getServer().getPluginManager();
    this.service = Bukkit.getServer().getServicesManager();

    this.executor = Executors.newCachedThreadPool();
    this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
  }

  public void register(final String prefix, final Class<?> clazz, final Object instance) {
    registerListeners(prefix, clazz, instance);
    registerCommands(prefix, clazz, instance);
  }

  @SafeVarargs
  public final void register(final String prefix, final Entry<Class<?>, Object>... entries) {
    registerListeners(prefix, entries);
    registerCommands(prefix, entries);
  }

  public void registerListeners(final String prefix, final Class<?> clazz, final Object instance) {
    registerListeners(prefix, new SimpleImmutableEntry<>(clazz, instance));
  }

  @SafeVarargs
  public final void registerListeners(final String prefix, final Entry<Class<?>, Object>... entries) {
    try {
      final Class<?>[] parameters = Arrays.stream(entries)
          .map(Entry::getKey)
          .toArray(Class[]::new);

      final Object[] instances = Arrays.stream(entries)
          .map(Entry::getValue)
          .toArray();

      final Reflections reflections = new Reflections(prefix);

      for (final Class<? extends Listener> clazz : reflections.getSubTypesOf(Listener.class)) {
        final Constructor<? extends Listener>[] constructors = Reflection.getConstructors(clazz);

        final Constructor<? extends Listener> constructor = Arrays.stream(constructors)
            .filter(newConstructor -> Arrays.equals(newConstructor.getParameterTypes(), parameters))
            .findFirst()
            .orElse(null);

        if (constructor == null) {
          continue;
        }

        manager.registerEvents(constructor.newInstance(instances), this);
      }
    } catch (Exception exception) {
      throw new InvalidListenerException("Unable to register listener: ", exception);
    }
  }

  public void registerCommands(final String prefix, final Class<?> clazz, final Object instance) {
    registerCommands(prefix, new SimpleImmutableEntry<>(clazz, instance));
  }

  @SafeVarargs
  public final void registerCommands(final String prefix, final Entry<Class<?>, Object>... entries) {
    try {
      final Class<?>[] parameters = Arrays.stream(entries)
          .map(Entry::getKey)
          .toArray(Class[]::new);

      final Object[] instances = Arrays.stream(entries)
          .map(Entry::getValue)
          .toArray();

      final Reflections reflections = new Reflections(prefix);

      final BukkitFrame frame = new BukkitFrame(this);
      final MessageHolder holder = frame.getMessageHolder();

      holder.setMessage(MessageType.ERROR, "§cA error occurred.");
      holder.setMessage(MessageType.INCORRECT_TARGET, "§cOnly {target} can execute this command.");
      holder.setMessage(MessageType.INCORRECT_USAGE, "§cWrong use! The correct is: /{usage}");
      holder.setMessage(MessageType.NO_PERMISSION, "§cYou don't have enough permissions.");

      for (final Class<?> clazz : reflections.getTypesAnnotatedWith(CommandHolder.class)) {
        final Constructor<?>[] constructors = Reflection.getConstructors(clazz);

        final Constructor<?> constructor = Arrays.stream(constructors)
            .filter(newConstructor -> Arrays.equals(newConstructor.getParameterTypes(), parameters))
            .findFirst()
            .orElse(null);

        if (constructor == null) {
          continue;
        }

        frame.registerCommands(constructor.newInstance(instances));
      }
    } catch (Exception exception) {
      throw new InvalidCommandException("Unable to register command: ", exception);
    }
  }

}
