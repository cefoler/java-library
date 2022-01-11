package com.celeste.library.spigot.model.scoreboard.impl;

import java.util.*;

import com.celeste.library.spigot.model.scoreboard.Board;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@Getter
@Setter
public final class BoardImpl implements Board {

  private String title;

  private Scoreboard scoreboard;
  private Objective objective;

  private final DisplaySlot slot;
  private List<String> lines;

  public BoardImpl(final Player player) {
    this("", player);
  }

  public BoardImpl(final String title, final Player player) {
    this(title, Bukkit.getScoreboardManager().getNewScoreboard(), player);
  }

  public BoardImpl(final String title, final Scoreboard scoreboard, final Player player) {
    this(title, scoreboard, scoreboard.registerNewObjective("score", "dummy"), player);
  }

  public BoardImpl(final String title, final DisplaySlot slot, final Player player) {
    this(title, Bukkit.getScoreboardManager().getNewScoreboard(), slot, player);
  }

  public BoardImpl(final String title, final Scoreboard scoreboard,
                   final DisplaySlot slot, final Player player) {
    this(title, scoreboard, scoreboard.registerNewObjective("score", "dummy"), slot, player);
  }

  public BoardImpl(final String title, final Scoreboard scoreboard,
                   final Objective objective, final Player player) {
    this(title, scoreboard, objective, DisplaySlot.SIDEBAR, player);
  }

  public BoardImpl(final String title, final Scoreboard scoreboard,
                   final Objective objective, final DisplaySlot slot, final Player player) {
    this(title, scoreboard, objective, slot, new ArrayList<>(), player);
  }

  public BoardImpl(final String title, final Scoreboard scoreboard, final Objective objective,
                   final DisplaySlot slot, final List<String> lines, final Player player) {
    this.title = title;
    this.scoreboard = scoreboard;
    this.objective = objective;
    this.slot = slot;
    this.lines = lines;

    objective.setDisplaySlot(slot);
    objective.setDisplayName(title);

    player.setScoreboard(scoreboard);
  }

  public void delete() {
    scoreboard.getEntries().forEach(scoreboard::resetScores);
  }

  public void set(final int line, final String text) {
    final Objective objective = getObjective();
    final Team team = getScoreboard().registerNewTeam(String.valueOf(UUID.randomUUID())
        .replace("-", "")
        .substring(0, 16));

    if (text.length() <= 16) {
      team.addEntry(text);

      objective.getScore(text).setScore(line);
      return;
    }

    if (text.length() <= 32) {
      final String prefix = text.substring(0, 16);
      final String entry = ChatColor.getLastColors(prefix) + text.substring(16);

      team.addEntry(entry);
      team.setPrefix(prefix);

      objective.getScore(entry).setScore(line);
      return;
    }

    final String prefix = text.substring(0, 16);
    final String entry = ChatColor.getLastColors(prefix) + text.substring(16, 32);
    final String suffix = ChatColor.getLastColors(entry) + text.substring(32);

    team.addEntry(entry);
    team.setPrefix(prefix);
    team.setSuffix(suffix);

    objective.getScore(entry).setScore(line);
  }

  @Override
  public void set(final String... texts) {
    final List<String> list = Arrays.asList(texts);
    Collections.reverse(list);

    for (int i = 0; i < list.size(); i++) {
      set(i, list.get(i));
    }

    this.lines = list;
  }

  @Override
  public void set(final Collection<String> texts) {
    final List<String> list = new ArrayList<>(texts);
    Collections.reverse(list);

    for (int i = 0; i < list.size(); i++) {
      set(i, list.get(i));
    }

    this.lines = list;
  }

  @Override
  public void remove(final int line) {
    lines.remove(line);
    set(lines);
  }

  @Override
  public String get(final int line) {
    return lines.get(line);
  }


  @Override
  public int size() {
    return lines.size();
  }

  @Override
  public void update(final int line, final String text) {

  }

}