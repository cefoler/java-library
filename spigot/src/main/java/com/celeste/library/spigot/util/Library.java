package com.celeste.library.spigot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Library {

  private static List<Player> getPlayers(final List<UUID> players) {
    return players.stream()
        .map(Bukkit::getPlayer)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private static ConsoleCommandSender getConsole() {
    return Bukkit.getServer().getConsoleSender();
  }

}
