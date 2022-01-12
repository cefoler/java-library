package com.celeste.library.spigot;

import com.celeste.library.core.factory.ThreadingFactory;
import com.celeste.library.spigot.util.item.enchantments.Glow;
import com.celeste.library.spigot.view.listener.MenuListener;
import com.celeste.library.spigot.view.listener.MonitorListener;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import static me.saiintbrisson.minecraft.command.message.MessageType.*;

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
  }

  @Override
  public void onEnable() {
    registerSystems();
    loadEnchantments();
  }

  @Override
  public void onDisable() {
    shutdownExecutors();
  }

  public void registerSystems() {
    new MenuListener(this);
    new MonitorListener(this);
  }

  public void shutdownExecutors() {
    EXECUTOR.shutdown();
    SCHEDULED.shutdown();
  }

  private void loadEnchantments() {
    final Glow glow = new Glow(1);
    Enchantment.registerEnchantment(glow);
  }

  public void registerListeners(final Listener... listeners) {
    Arrays.stream(listeners).forEach(listener -> manager.registerEvents(listener, this));
  }

  public void registerCommands(final Object... instances) {
    final BukkitFrame frame = new BukkitFrame(this);
    final MessageHolder holder = frame.getMessageHolder();

    holder.setMessage(ERROR, "§cA error occurred.");
    holder.setMessage(INCORRECT_TARGET, "§cOnly {target} can execute this command..");
    holder.setMessage(INCORRECT_USAGE, "§cWrong use! The correct is: /{usage}");
    holder.setMessage(NO_PERMISSION, "§cYou don't have enough permissions.");

    frame.registerCommands(instances);
  }

  public void registerCommands(final String[] messages, final Object... instances) {
    final BukkitFrame frame = new BukkitFrame(this);
    final MessageHolder holder = frame.getMessageHolder();

    holder.setMessage(ERROR, messages[0]);
    holder.setMessage(INCORRECT_TARGET, messages[1]);
    holder.setMessage(INCORRECT_USAGE, messages[2]);
    holder.setMessage(NO_PERMISSION, messages[3]);

    frame.registerCommands(instances);
  }

  public static ExecutorService getExecutor() {
    return EXECUTOR;
  }

  public static ScheduledExecutorService getScheduled() {
    return SCHEDULED;
  }

}
