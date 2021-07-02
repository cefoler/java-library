package com.celeste.library.bungee;

import com.celeste.library.bungee.annotation.CommandHolder;
import com.celeste.library.bungee.exception.InvalidCommandException;
import com.celeste.library.bungee.exception.InvalidListenerException;
import com.celeste.library.core.factory.ThreadingFactory;
import com.celeste.library.core.util.Reflection;
import java.lang.reflect.Constructor;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.reflections.Reflections;

@Getter
public abstract class AbstractBungeePlugin extends Plugin {

  private static final ExecutorService EXECUTOR;
  private static final ScheduledExecutorService SCHEDULED;

  static {
    EXECUTOR = ThreadingFactory.threadPool();
    SCHEDULED = ThreadingFactory.scheduledThreadPool();
  }

  private final PluginManager manager;

  public AbstractBungeePlugin() {
    this.manager = getProxy().getPluginManager();
  }

  public void registerListeners(final Listener... listeners) {
    Arrays.stream(listeners).forEach(listener -> manager.registerListener(this, listener));
  }

  public void registerCommands(final Object... instances) {
    final BungeeFrame frame = new BungeeFrame(this);
    final MessageHolder holder = frame.getMessageHolder();

    holder.setMessage(MessageType.ERROR, "§cA error occurred.");
    holder.setMessage(MessageType.INCORRECT_TARGET, "Only {target} can execute this command..");
    holder.setMessage(MessageType.INCORRECT_USAGE, "Wrong use! The correct is: /{usage}");
    holder.setMessage(MessageType.NO_PERMISSION, "You don't have enough permissions.");

    frame.registerCommands(instances);
  }

  public void registerCommands(final String[] messages, final Object... instances) {
    final BungeeFrame frame = new BungeeFrame(this);
    final MessageHolder holder = frame.getMessageHolder();

    holder.setMessage(MessageType.ERROR, messages[0]);
    holder.setMessage(MessageType.INCORRECT_TARGET, messages[1]);
    holder.setMessage(MessageType.INCORRECT_USAGE, messages[2]);
    holder.setMessage(MessageType.NO_PERMISSION, messages[3]);

    frame.registerCommands(instances);
  }

  @Deprecated
  public void register(final String path, final Class<?> clazz, final Object instance) {
    registerListeners(path, clazz, instance);
    registerCommands(path, clazz, instance);
  }

  @Deprecated
  @SafeVarargs
  public final void register(final String path, final Entry<Class<?>, Object>... entries) {
    registerListeners(path, entries);
    registerCommands(path, entries);
  }

  @Deprecated
  public void registerListeners(final String path, final Class<?> clazz, final Object instance) {
    registerListeners(path, new SimpleImmutableEntry<>(clazz, instance));
  }

  @Deprecated
  @SafeVarargs
  public final void registerListeners(final String path, final Entry<Class<?>, Object>... entries) {
    try {
      final Class<?>[] parameters = Arrays.stream(entries)
          .map(Entry::getKey)
          .toArray(Class[]::new);

      final Object[] instances = Arrays.stream(entries)
          .map(Entry::getValue)
          .toArray();

      final Reflections reflections = new Reflections(path);

      for (final Class<? extends Listener> clazz : reflections.getSubTypesOf(Listener.class)) {
        final Constructor<? extends Listener>[] constructors = Reflection.getConstructors(clazz);

        final Constructor<? extends Listener> constructor = Arrays.stream(constructors)
            .filter(newConstructor -> Arrays.equals(newConstructor.getParameterTypes(), parameters))
            .findFirst()
            .orElse(null);

        if (constructor == null) {
          continue;
        }

        manager.registerListener(this, constructor.newInstance(instances));
      }
    } catch (Exception exception) {
      throw new InvalidListenerException("Unable to register listener: ", exception);
    }
  }

  @Deprecated
  public void registerCommands(final String path, final Class<?> clazz, final Object instance) {
    registerCommands(path, new SimpleImmutableEntry<>(clazz, instance));
  }

  @Deprecated
  @SafeVarargs
  public final void registerCommands(final String path, final Entry<Class<?>, Object>... entries) {
    try {
      final Class<?>[] parameters = Arrays.stream(entries)
          .map(Entry::getKey)
          .toArray(Class[]::new);

      final Object[] instances = Arrays.stream(entries)
          .map(Entry::getValue)
          .toArray();

      final Reflections reflections = new Reflections(path);

      final BungeeFrame frame = new BungeeFrame(this);
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

  public static ExecutorService getExecutor() {
    return EXECUTOR;
  }

  public static ScheduledExecutorService getScheduled() {
    return SCHEDULED;
  }

}
