package com.celeste.library.spigot.model.timer;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

import static com.celeste.library.spigot.AbstractBukkitPlugin.*;

@Data
public abstract class Timer {

  protected int time;
  protected Plugin plugin;

  protected static BukkitTask TASK;

  public void start() {
    start(20, 20);
  }

  public void start(final long delay, final long period) {
    TASK = Bukkit.getScheduler().runTaskTimer(plugin, this::runSecond, delay, period);
  }

  public void startAsync() {
    getScheduled().scheduleWithFixedDelay(this::runSecond, 0, 1, TimeUnit.SECONDS);
  }

  public void startASync(final long delay, final long period, final TimeUnit unit) {
    getScheduled().scheduleWithFixedDelay(this::runSecond, delay, period, unit);
  }

  protected abstract void runSecond();

  public static void end() {
    if (TASK != null) {
      TASK.cancel();
    }
  }

}
