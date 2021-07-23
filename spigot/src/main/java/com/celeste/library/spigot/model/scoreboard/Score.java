package com.celeste.library.spigot.model.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
public class Score {

  private final String title;

  private Scoreboard scoreboard;
  private Objective objective;

  private final DisplaySlot slot;
  private List<String> lines;

  public Score(final String title) {
    this(title, Bukkit.getScoreboardManager().getNewScoreboard());
  }

  public Score(final String title, final Scoreboard scoreboard) {
    this(title, scoreboard, scoreboard.registerNewObjective("score", "dummy"));
  }

  public Score(final String title, final DisplaySlot slot) {
    this(title, Bukkit.getScoreboardManager().getNewScoreboard(), slot);
  }

  public Score(final String title, final Scoreboard scoreboard,
      final DisplaySlot slot) {
    this(title, scoreboard, scoreboard.registerNewObjective("score", "dummy"), slot);
  }

  public Score(final String title, final Scoreboard scoreboard,
      final Objective objective) {
    this(title, scoreboard, objective, DisplaySlot.SIDEBAR);
  }

  public Score(final String title, final Scoreboard scoreboard,
      final Objective objective, final DisplaySlot slot) {
    this(title, scoreboard, objective, slot, new ArrayList<>());
  }

  public Score(final String title, final Scoreboard scoreboard, final Objective objective,
      final DisplaySlot slot, final List<String> lines) {
    this.title = title;
    this.scoreboard = scoreboard;
    this.objective = objective;
    this.slot = slot;
    this.lines = lines;

    objective.setDisplaySlot(slot);
    objective.setDisplayName(title);
  }

  public void send(final Player player) {
    player.setScoreboard(scoreboard);
  }

  public void clear() {
    scoreboard.getEntries().forEach(scoreboard::resetScores);
  }

  private void setLine(final Score score, final int line, final String text) {
    final Objective objective = score.getObjective();
    final Team team = score.getScoreboard().registerNewTeam(String.valueOf(UUID.randomUUID())
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

  private void setLine(final Score score, final int line, final String prefix,
      final String suffix) {
    final Objective objective = score.getObjective();
    final Team team = score.getScoreboard().registerNewTeam(String.valueOf(line));

    if (suffix.length() <= 16) {
      team.addEntry(prefix);
      team.setSuffix(suffix);

      objective.getScore(prefix).setScore(line);
      return;
    }

    final String newPrefix = suffix.substring(0, 16);
    final String newSuffix = ChatColor.getLastColors(newPrefix) + suffix.substring(16);

    team.addEntry(prefix);
    team.setPrefix(newPrefix);
    team.setSuffix(newSuffix);

    objective.getScore(prefix).setScore(line);
  }

  public void updateLine(final Score score, final int line, final String prefix,
      final String suffix) {
    final Objective objective = score.getObjective();
    final Team team = score.getScoreboard().getTeam(String.valueOf(line));

    team.setPrefix("");
    team.setSuffix("");

    if (suffix.length() <= 16) {
      team.setSuffix(suffix);

      objective.getScore(prefix).setScore(line);
      return;
    }

    final String newPrefix = suffix.substring(0, 16);
    final String newSuffix = ChatColor.getLastColors(newPrefix) + suffix.substring(16);

    team.setPrefix(newPrefix);
    team.setSuffix(newSuffix);

    objective.getScore(prefix).setScore(line);
  }

}