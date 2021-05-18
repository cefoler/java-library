package com.celeste.library.spigot.view.event.wrapper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractCancellableEvent extends AbstractEvent implements Cancellable {

  private boolean cancelled;

}
