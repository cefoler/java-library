package com.celeste.library.spigot.model.timer;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

import static com.celeste.library.spigot.AbstractBukkitPlugin.*;

@Data
public abstract class AbstractTimer implements Timer {

  protected Plugin plugin;

  protected static BukkitTask TASK;
  protected static boolean CANCELLED;

  static {
    TASK = null;
    CANCELLED = true;
  }

  public abstract void run();

  public void start() {
    start(20, 20);
  }

  public void start(final long delay, final long period) {
    CANCELLED = false;
    TASK = Bukkit.getScheduler().runTaskTimer(plugin, this::run, delay, period);
  }

  public void startAsync() {
    getScheduled().scheduleWithFixedDelay(this::run, 0, 1, TimeUnit.SECONDS);
  }

  public void startAsync(final long delay, final long period, final TimeUnit unit) {
    getScheduled().scheduleWithFixedDelay(this::run, delay, period, unit);
  }

  public static void end() {
    if (TASK != null) {
      CANCELLED = true;
      TASK.cancel();
    }
  }

  public static boolean isCancelled() {
    return CANCELLED;
  }

}
