package com.celeste.library.bungee;

import com.celeste.library.bungee.annotation.CommandHolder;
import com.celeste.library.bungee.exception.InvalidCommandException;
import com.celeste.library.bungee.exception.InvalidListenerException;
import lombok.Getter;
import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static me.saiintbrisson.minecraft.command.message.MessageType.*;
import static me.saiintbrisson.minecraft.command.message.MessageType.NO_PERMISSION;

@Getter
public abstract class AbstractBungeePlugin extends Plugin {

  private final PluginManager manager;

  public AbstractBungeePlugin() {
    this.manager = getProxy().getPluginManager();
  }

  public <T extends AbstractBungeePlugin> void registerListeners(@NotNull final Class<T> plugin, @NotNull final T instance) {
    try {
      for (final Class<? extends Listener> clazz : new Reflections("").getSubTypesOf(Listener.class)) {
        final Constructor<? extends Listener> constructor = (Constructor<? extends Listener>) clazz.getConstructors()[0];

        final Listener listener = constructor.getParameterCount() != 0
            ? Arrays.asList(constructor.getParameterTypes()).contains(plugin)
            ? constructor.newInstance(instance)
            : null
            : constructor.newInstance();

        if (listener == null) continue;

        manager.registerListener(instance, listener);
      }
    } catch (Throwable throwable) {
      throw new InvalidListenerException("Unable to register listener: ", throwable);
    }

  }

  /**
   * Starts the BukkitFrame and MessageHolder
   * With the default messages
   */
  public <T extends AbstractBungeePlugin> void registerCommands(@NotNull final Class<T> plugin, @NotNull final T instance) {
    try {
      final BungeeFrame frame = new BungeeFrame(this);
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
