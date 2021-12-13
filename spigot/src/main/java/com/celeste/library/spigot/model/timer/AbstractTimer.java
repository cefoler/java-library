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

  protected BukkitTask task;
  protected boolean cancelled;

  public AbstractTimer(final Plugin plugin) {
    this.plugin = plugin;
    this.task = null;
    this.cancelled = true;
  }

  public abstract void run();

  public void start() {
    start(20, 20);
  }

  public void start(final long delay, final long period, final TimeUnit unit) {
    final long delayInSeconds = unit.convert(delay, TimeUnit.SECONDS) * 20;
    final long periodInSeconds = unit.convert(period, TimeUnit.SECONDS) * 20;

    start(delayInSeconds, periodInSeconds);
  }

  public void start(final long delay, final long period) {
    this.task = Bukkit.getScheduler().runTaskTimer(plugin, this::run, delay, period);
    this.cancelled = false;
  }

  public void startAsync() {
    startAsync(0, 1, TimeUnit.SECONDS);
  }

  public void startAsync(final long delay, final long period, final TimeUnit unit) {
    getScheduled().scheduleWithFixedDelay(this::run, delay, period, unit);
  }

  public void stop() {
    if (task == null) {
      return;
    }

    cancelled = true;
    task.cancel();
  }

  public boolean isCancelled() {
    return cancelled;
  }

}
