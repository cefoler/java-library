package com.celeste.library.spigot;

import com.celeste.library.core.factory.ThreadingFactory;
import com.celeste.library.spigot.view.listener.MenuListener;
import com.celeste.library.spigot.view.listener.MonitorListener;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
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

@Getter
public abstract class AbstractBukkitPlugin extends JavaPlugin {

  protected static final ExecutorService EXECUTOR;
  protected static final ScheduledExecutorService SCHEDULED;

  static {
    EXECUTOR = ThreadingFactory.threadPool();
    SCHEDULED = ThreadingFactory.scheduledThreadPool();
  }

  protected final PluginManager manager;
  protected final ServicesManager service;

  public AbstractBukkitPlugin() {
    this.manager = Bukkit.getServer().getPluginManager();
    this.service = Bukkit.getServer().getServicesManager();

    registerSystems();
  }

  private void registerSystems() {
    new MenuListener(this);
    new MonitorListener(this);
  }

  public void registerListeners(final Listener... listeners) {
    Arrays.stream(listeners).forEach(listener -> manager.registerEvents(listener, this));
  }

  public void registerCommands(final Object... instances) {
    final BukkitFrame frame = new BukkitFrame(this);
    final MessageHolder holder = frame.getMessageHolder();

    holder.setMessage(MessageType.ERROR, "§cA error occurred.");
    holder.setMessage(MessageType.INCORRECT_TARGET, "Only {target} can execute this command..");
    holder.setMessage(MessageType.INCORRECT_USAGE, "Wrong use! The correct is: /{usage}");
    holder.setMessage(MessageType.NO_PERMISSION, "You don't have enough permissions.");

    frame.registerCommands(instances);
  }

  public void registerCommands(final String[] messages, final Object... instances) {
    final BukkitFrame frame = new BukkitFrame(this);
    final MessageHolder holder = frame.getMessageHolder();

    holder.setMessage(MessageType.ERROR, messages[0]);
    holder.setMessage(MessageType.INCORRECT_TARGET, messages[1]);
    holder.setMessage(MessageType.INCORRECT_USAGE, messages[2]);
    holder.setMessage(MessageType.NO_PERMISSION, messages[3]);

    frame.registerCommands(instances);
  }

  public static ExecutorService getExecutor() {
    return EXECUTOR;
  }

  public static ScheduledExecutorService getScheduled() {
    return SCHEDULED;
  }

}
