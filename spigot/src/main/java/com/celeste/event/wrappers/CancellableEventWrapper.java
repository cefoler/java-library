package com.celeste.event.wrappers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class CancellableEventWrapper extends EventWrapper implements Cancellable {

    private boolean cancelled;

}
