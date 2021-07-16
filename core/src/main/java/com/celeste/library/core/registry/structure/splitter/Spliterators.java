package com.celeste.library.core.registry.structure.splitter;

import com.celeste.library.core.registry.collection.Collection;
import com.celeste.library.core.registry.structure.splitter.impl.IteratorSplitter;
import java.util.Spliterator;
import org.jetbrains.annotations.NotNull;

public final class Spliterators {

  public static <T> Spliterator<T> spliterator(@NotNull final Collection<? extends T> collection, int characteristics) {
    return new IteratorSplitter<>(collection, characteristics);
  }

}
