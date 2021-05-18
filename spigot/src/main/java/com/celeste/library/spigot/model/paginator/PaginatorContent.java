package com.celeste.library.spigot.model.paginator;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginatorContent<T> {

  private final Integer[] shape;
  private final List<T> source;

}
