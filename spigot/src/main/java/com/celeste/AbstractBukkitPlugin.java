package com.celeste;

import com.celeste.annotation.CommandHolder;
import com.celeste.exception.InvalidCommandException;
import com.celeste.exception.InvalidListenerException;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import static me.saiintbrisson.minecraft.command.message.MessageType.*;

public abstract class AbstractBukkitPlugin extends JavaPlugin {

    public <T extends AbstractBukkitPlugin> void registerListeners(final Class<T> plugin, final T instance) {
      final PluginManager manager = getServer().getPluginManager();

      try {
        for (Class<? extends Listener> clazz : new Reflections("").getSubTypesOf(Listener.class)) {
          final Listener listener = clazz.getConstructor(plugin).newInstance(instance);
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
    public <T extends AbstractBukkitPlugin> void registerCommands(final Class<T> plugin, final T instance) {
      final BukkitFrame frame = new BukkitFrame(this);
      final MessageHolder holder = frame.getMessageHolder();

      holder.setMessage(ERROR, "§cA error occurred.");
      holder.setMessage(INCORRECT_TARGET, "§cOnly players can execute this command..");
      holder.setMessage(INCORRECT_USAGE, "§cWrong use! The correct is: /{usage}");
      holder.setMessage(NO_PERMISSION, "§cYou don't have enough permissions.");

      try {
        for (Class<?> clazz : new Reflections("").getTypesAnnotatedWith(CommandHolder.class)) {
          frame.registerCommands(clazz.getConstructor(plugin).newInstance(instance));
        }
      } catch (Throwable throwable) {
        throw new InvalidCommandException("Unable to register command: ", throwable);
      }
    }

}
