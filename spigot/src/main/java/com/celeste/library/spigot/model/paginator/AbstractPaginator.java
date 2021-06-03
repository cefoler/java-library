package com.celeste.library.spigot.model.paginator;

import com.celeste.library.spigot.model.menu.MenuHolder;
import java.util.List;
import lombok.Data;

@Data
public abstract class AbstractPaginator<T> implements Paginator<T> {

  protected final MenuHolder holder;

  protected final int[] shape;
  protected final List<T> source;

}
