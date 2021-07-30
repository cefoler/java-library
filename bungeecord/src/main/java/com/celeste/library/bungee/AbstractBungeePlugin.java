package com.celeste.library.bungee;

import com.celeste.library.core.factory.ThreadingFactory;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

@Getter
public abstract class AbstractBungeePlugin extends Plugin {

  protected static final ExecutorService EXECUTOR;
  protected static final ScheduledExecutorService SCHEDULED;

  static {
    EXECUTOR = ThreadingFactory.threadPool();
    SCHEDULED = ThreadingFactory.scheduledThreadPool();
  }

  protected final PluginManager manager;

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
    holder.setMessage(MessageType.INCORRECT_TARGET, "§cOnly {target} can execute this command..");
    holder.setMessage(MessageType.INCORRECT_USAGE, "§cWrong use! The correct is: /{usage}");
    holder.setMessage(MessageType.NO_PERMISSION, "§cYou don't have enough permissions.");

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

  public static ExecutorService getExecutor() {
    return EXECUTOR;
  }

  public static ScheduledExecutorService getScheduled() {
    return SCHEDULED;
  }

}
