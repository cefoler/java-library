package com.celeste.library.spigot.view.event.wrapper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractCancellableEvent extends Event implements Cancellable {

  private boolean cancelled;

}
