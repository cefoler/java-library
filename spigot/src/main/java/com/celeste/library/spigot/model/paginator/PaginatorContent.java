package com.celeste.library.spigot.model.paginator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatorContent<T> {

  private final Integer[] shape;
  private final List<T> source;

}
