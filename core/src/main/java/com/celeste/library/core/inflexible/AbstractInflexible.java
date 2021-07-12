package com.celeste.library.core.inflexible;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

@EqualsAndHashCode(callSuper = false)
public abstract class AbstractInflexible<E> extends AbstractCollection<E> implements Inflexible<E> {

  @Override
  public boolean removeAll(@NotNull Collection<?> collection) {
    boolean modified = false;

    if (size() > collection.size()) {
      for (Object object : collection) {
        modified |= remove(object);
      }

      return modified;
    }

    for (Iterator<?> iterator = iterator(); iterator.hasNext(); ) {
      if (!collection.contains(iterator.next())) {
        continue;
      }

      iterator.remove();
      modified = true;
    }

    return modified;
  }

}
