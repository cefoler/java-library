package com.celeste.library.spigot.model.paginator;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class AbstractPaginator<T> implements Paginator<T> {

  private final int[] shape;
  private final List<T> source;

}
