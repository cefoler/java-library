package com.celeste.library.spigot.model.scoreboard;

import java.util.Collection;

public interface Board {

  void setTitle(final String title);

  void set(final String... texts);

  void set(final Collection<String> texts);

  void update(final int line, final String text);

  void remove(final int line);

  String get(final int line);

  void delete();

  int size();

}
