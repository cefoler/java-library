package com.celeste.library.core.factory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadingFactory {

  public static ExecutorService threadPool() {
    return threadPool(0, Integer.MAX_VALUE, 5L, TimeUnit.MINUTES);
  }

  public static ExecutorService threadPool(final ThreadFactory factory) {
    return threadPool(0, Integer.MAX_VALUE, 5L, TimeUnit.MINUTES, factory);
  }

  public static ExecutorService threadPool(final int minThreads) {
    return threadPool(minThreads, Integer.MAX_VALUE, 5L, TimeUnit.MINUTES);
  }

  public static ExecutorService threadPool(final int minThreads, final ThreadFactory factory) {
    return threadPool(minThreads, Integer.MAX_VALUE, 5L, TimeUnit.MINUTES, factory);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads) {
    return threadPool(minThreads, maxThreads, 5L, TimeUnit.MINUTES);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final ThreadFactory factory) {
    return threadPool(minThreads, maxThreads, 5L, TimeUnit.MINUTES, factory);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final long keepAlive, final TimeUnit time) {
    return threadPool(minThreads, maxThreads, keepAlive, time, new SynchronousQueue<>());
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final long keepAlive, final TimeUnit time, final ThreadFactory factory) {
    return threadPool(minThreads, maxThreads, keepAlive, time, new SynchronousQueue<>(), factory);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final Supplier<BlockingQueue<Runnable>> queue) {
    return threadPool(minThreads, maxThreads, 5L, TimeUnit.MINUTES, queue.get());
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final Supplier<BlockingQueue<Runnable>> queue, final ThreadFactory factory) {
    return threadPool(minThreads, maxThreads, 5L, TimeUnit.MINUTES, queue.get(), factory);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final BlockingQueue<Runnable> queue) {
    return threadPool(minThreads, maxThreads, 5L, TimeUnit.MINUTES, queue);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final BlockingQueue<Runnable> queue, final ThreadFactory factory) {
    return threadPool(minThreads, maxThreads, 5L, TimeUnit.MINUTES, queue, factory);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final long keepAlive, final TimeUnit time, final Supplier<BlockingQueue<Runnable>> queue) {
    return threadPool(minThreads, maxThreads, keepAlive, time, queue.get());
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final long keepAlive, final TimeUnit time, final Supplier<BlockingQueue<Runnable>> queue,
      final ThreadFactory factory) {
    return threadPool(minThreads, maxThreads, keepAlive, time, queue.get(), factory);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final long keepAlive, final TimeUnit time, final BlockingQueue<Runnable> queue) {
    return new ThreadPoolExecutor(minThreads, maxThreads, keepAlive, time, queue);
  }

  public static ExecutorService threadPool(final int minThreads, final int maxThreads,
      final long keepAlive, final TimeUnit time, final BlockingQueue<Runnable> queue,
      final ThreadFactory factory) {
    return new ThreadPoolExecutor(minThreads, maxThreads, keepAlive, time, queue, factory);
  }

  public static ExecutorService fixedThreadPool(final int threads) {
    return fixedThreadPool(threads, 0L, TimeUnit.MILLISECONDS);
  }

  public static ExecutorService fixedThreadPool(final int threads, final long keepAlive,
      final TimeUnit time) {
    return fixedThreadPool(threads, keepAlive, time, new SynchronousQueue<>());
  }

  public static ExecutorService fixedThreadPool(final int threads, final long keepAlive,
      final TimeUnit time, final ThreadFactory factory) {
    return fixedThreadPool(threads, keepAlive, time, new SynchronousQueue<>(), factory);
  }

  public static ExecutorService fixedThreadPool(final int threads,
      final Supplier<BlockingQueue<Runnable>> queue) {
    return fixedThreadPool(threads, 0L, TimeUnit.MILLISECONDS, queue.get());
  }

  public static ExecutorService fixedThreadPool(final int threads,
      final Supplier<BlockingQueue<Runnable>> queue, final ThreadFactory factory) {
    return fixedThreadPool(threads, 0L, TimeUnit.MILLISECONDS, queue.get(), factory);
  }

  public static ExecutorService fixedThreadPool(final int threads,
      final BlockingQueue<Runnable> queue) {
    return fixedThreadPool(threads, 0L, TimeUnit.MILLISECONDS, queue);
  }

  public static ExecutorService fixedThreadPool(final int threads,
      final BlockingQueue<Runnable> queue, final ThreadFactory factory) {
    return fixedThreadPool(threads, 0L, TimeUnit.MILLISECONDS, queue, factory);
  }

  public static ExecutorService fixedThreadPool(final int threads, final long keepAlive,
      final TimeUnit time, final Supplier<BlockingQueue<Runnable>> queue) {
    return fixedThreadPool(threads, keepAlive, time, queue.get());
  }

  public static ExecutorService fixedThreadPool(final int threads, final long keepAlive,
      final TimeUnit time, final Supplier<BlockingQueue<Runnable>> queue,
      final ThreadFactory factory) {
    return fixedThreadPool(threads, keepAlive, time, queue.get(), factory);
  }

  public static ExecutorService fixedThreadPool(final int threads, final long keepAlive,
      final TimeUnit time, final BlockingQueue<Runnable> queue) {
    return new ThreadPoolExecutor(threads, threads, keepAlive, time, queue);
  }

  public static ExecutorService fixedThreadPool(final int threads, final long keepAlive,
      final TimeUnit time, final BlockingQueue<Runnable> queue, final ThreadFactory factory) {
    return new ThreadPoolExecutor(threads, threads, keepAlive, time, queue, factory);
  }

  public static ScheduledExecutorService scheduledThreadPool() {
    return new ScheduledThreadPoolExecutor(1);
  }

  public static ScheduledExecutorService scheduledThreadPool(final int minThreads) {
    return new ScheduledThreadPoolExecutor(minThreads);
  }

  public static ScheduledExecutorService scheduledThreadPool(final int minThreads,
      final ThreadFactory factory) {
    return new ScheduledThreadPoolExecutor(minThreads, factory);
  }

}
