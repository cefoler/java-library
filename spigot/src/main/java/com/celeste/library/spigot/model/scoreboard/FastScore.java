package com.celeste.library.spigot.model.scoreboard;

import com.celeste.library.spigot.util.ReflectionNms;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.celeste.library.core.util.Reflection.*;
import static com.celeste.library.spigot.model.scoreboard.FastScore.ObjectiveMode.*;
import static com.celeste.library.spigot.model.scoreboard.FastScore.VersionType.*;

@Getter
public class FastScore {

  private static final Map<Class<?>, Field[]> PACKETS;
  private static final String[] COLOR_CODES;
  private static final VersionType VERSION_TYPE;

  private static final Class<?> CHAT_COMPONENT_CLASS;
  private static final Class<?> CHAT_FORMAT_ENUM;

  private static final Object EMPTY_MESSAGE;
  private static final Object RESET_FORMATTING;

  private static final MethodHandle MESSAGE_FROM_STRING;
  private static final MethodHandle PLAYER_CONNECTION;
  private static final MethodHandle SEND_PACKET;
  private static final MethodHandle PLAYER_GET_HANDLE;

  private static final Constructor<?> PACKET_SB_OBJ;
  private static final Constructor<?> PACKET_SB_DISPLAY_OBJ;
  private static final Constructor<?> PACKET_SB_SCORE;
  private static final Constructor<?> PACKET_SB_TEAM;

  private static Class<?> ENUM_SB_HEALTH_DISPLAY;
  private static Class<?> ENUM_SB_ACTION;
  private static Object ENUM_SB_HEALTH_DISPLAY_INTEGER;
  private static Object ENUM_SB_ACTION_CHANGE;
  private static Object ENUM_SB_ACTION_REMOVE;

  static {
    PACKETS = new HashMap<>(8);
    COLOR_CODES = Arrays.stream(ChatColor.values())
        .map(Object::toString)
        .toArray(String[]::new);
  }

  protected final Player player;
  private final String id;

  private final List<String> lines;
  private String title;

  private boolean deleted;

  static {
    try {
      if (ReflectionNms.isEqualsOrMoreRecent(13)) {
        VERSION_TYPE = V1_13;
      } else {
        VERSION_TYPE = V1_8;
      }

      final String gameProtocolPackage = "network.protocol.game.";

      final Class<?> entityPlayerClass = ReflectionNms.getNms("server.level.EntityPlayer");
      final Class<?> playerConnectionClass = ReflectionNms.getNms("server.network.PlayerConnection");
      final Class<?> packetClass = ReflectionNms.getNms("network.protocol.Packet");
      final Class<?> packetSbObjClass = ReflectionNms.getNms(gameProtocolPackage + "PacketPlayOutScoreboardObjective");
      final Class<?> packetSbDisplayObjClass = ReflectionNms.getNms(gameProtocolPackage + "PacketPlayOutScoreboardDisplayObjective");
      final Class<?> packetSbScoreClass = ReflectionNms.getNms(gameProtocolPackage + "PacketPlayOutScoreboardScore");
      final Class<?> packetSbTeamClass = ReflectionNms.getNms(gameProtocolPackage + "PacketPlayOutScoreboardTeam");

      final Field playerConnectionField = Arrays.stream(entityPlayerClass.getFields())
          .filter((field) -> field.getType().isAssignableFrom(playerConnectionClass))
          .findFirst()
          .orElseThrow(NoSuchFieldException::new);

      final MethodHandles.Lookup lookup = MethodHandles.lookup();

      MESSAGE_FROM_STRING = lookup.unreflect(ReflectionNms.getObc("util.CraftChatMessage").getMethod("fromString", String.class));
      CHAT_COMPONENT_CLASS = ReflectionNms.getNms("network.chat.IChatBaseComponent");
      CHAT_FORMAT_ENUM = ReflectionNms.getNms("EnumChatFormat");
      EMPTY_MESSAGE = Array.get(MESSAGE_FROM_STRING.invoke(""), 0);
      RESET_FORMATTING = getValueFromEnum(CHAT_FORMAT_ENUM, "RESET", 21);
      PLAYER_GET_HANDLE = lookup.findVirtual(ReflectionNms.getObc("entity.CraftPlayer"), "getHandle", MethodType.methodType(entityPlayerClass));
      PLAYER_CONNECTION = lookup.unreflectGetter(playerConnectionField);
      SEND_PACKET = lookup.findVirtual(playerConnectionClass, "sendPacket", MethodType.methodType(Void.TYPE, packetClass));

      PACKET_SB_OBJ = packetSbObjClass.getConstructor();
      PACKET_SB_DISPLAY_OBJ = packetSbDisplayObjClass.getConstructor();
      PACKET_SB_SCORE = packetSbScoreClass.getConstructor();
      PACKET_SB_TEAM = packetSbTeamClass.getConstructor();

      final Iterator<Class<?>> iterator = Arrays.asList(packetSbObjClass, packetSbDisplayObjClass, packetSbScoreClass, packetSbTeamClass).iterator();

      boolean canDo = true;
      while(canDo) {
        Class<?> clazz = null;
        do {
          if (!iterator.hasNext()) {
            if (!V1_8.isHigherOrEqual()) {
              continue;
            }

            final String enumSbActionClass = V1_13.isHigherOrEqual()
                ? "ScoreboardServer$Action"
                : "PacketPlayOutScoreboardScore$EnumScoreboardAction";

            ENUM_SB_HEALTH_DISPLAY = ReflectionNms.getNms("world.scores.criteria.IScoreboardCriteria$EnumScoreboardHealthDisplay");
            ENUM_SB_ACTION = ReflectionNms.getNms("server." + enumSbActionClass);
            ENUM_SB_HEALTH_DISPLAY_INTEGER = getValueFromEnum(ENUM_SB_HEALTH_DISPLAY, "INTEGER", 0);
            ENUM_SB_ACTION_CHANGE = getValueFromEnum(ENUM_SB_ACTION, "CHANGE", 0);
            ENUM_SB_ACTION_REMOVE = getValueFromEnum(ENUM_SB_ACTION, "REMOVE", 1);
            continue;
          }

          clazz = iterator.next();
        } while (clazz == null);

        final Field[] fields = Arrays.stream(clazz.getDeclaredFields())
            .filter((field) -> !Modifier.isStatic(field.getModifiers()))
            .toArray(Field[]::new);

        for (final Field field : fields) {
          field.setAccessible(true);
        }

        PACKETS.put(clazz, fields);
        canDo = false;
      }
    } catch (Throwable throwable) {
      throw new ExceptionInInitializerError(throwable);
    }
  }

  public FastScore(final Player player) {
    this.title = ChatColor.RESET.toString();
    this.deleted = false;
    this.player = player;
    this.lines = new ArrayList<>(16);

    this.id = "fs-" + Integer.toHexString(ThreadLocalRandom.current().nextInt());

    sendObjectivePacket(CREATE);
    sendDisplayObjectivePacket();
  }

  public void updateTitle(final String title) {
    if (getTitle().equals(title)) {
      return;
    }

    if (!V1_13.isHigherOrEqual() && title.length() > 32) {
      throw new IllegalArgumentException("Title is longer than 32 chars");
    }

    this.title = title;
    sendObjectivePacket(UPDATE);
  }

  public String getLine(final int line) {
    checkLineNumber(line, true, false);
    return lines.get(line);
  }

  public synchronized void updateLine(final int line, final String text) {
    checkLineNumber(line, false, true);

    if (line < size()) {
      lines.set(line, text);

      sendTeamPacket(getScoreByLine(line), TeamMode.UPDATE);
      return;
    }

    final List<String> newLines = new ArrayList<>(lines);
    if (line > size()) {
      for (int index = size(); index < line; index++) {
        newLines.add("");
      }
    }

    newLines.add(text);
    updateLines(newLines);
  }

  public synchronized void removeLine(final int line) {
    checkLineNumber(line, false, false);
    if (line > this.size()) {
      return;
    }

    final List<String> newLines = new ArrayList<>(lines);
    newLines.remove(line);
    
    updateLines(newLines);
  }

  public void updateLines(final String... lines) {
    updateLines(Arrays.asList(lines));
  }

  public synchronized void updateLines(@NotNull final Collection<String> newLines) {
    checkLineNumber(newLines.size(), false, true);
    if (!V1_13.isHigherOrEqual()) {
      int lineCount = 0;

      for (Iterator<String> iterator = newLines.iterator(); iterator.hasNext(); lineCount++) {
        final String string = iterator.next();
        if (string != null && string.length() > 30) {
          throw new IllegalArgumentException("Line " + lineCount + " is longer than 30 chars");
        }
      }
    }

    final List<String> oldLines = new ArrayList<>(lines);
    
    lines.clear();
    lines.addAll(newLines);

    final int linesSize = size();
    if (oldLines.size() == linesSize) {
      for (int i = 0; i < linesSize; i++) {
        if (!Objects.equals(getLineByScore(oldLines, i), getLineByScore(i))) {
          sendTeamPacket(i, TeamMode.UPDATE);
        }
      }

      return;
    }

    final List<String> oldLinesCopy = new ArrayList<>(oldLines);

    int index;
    if (oldLines.size() > linesSize) {
      for (index = oldLinesCopy.size(); index > linesSize; index--) {
        sendTeamPacket(index - 1, TeamMode.REMOVE);
        sendScorePacket(index - 1, ScoreboardAction.REMOVE);

        oldLines.remove(0);
      }

      return;
    }

    for (index = oldLinesCopy.size(); index < linesSize; index++) {
      sendScorePacket(index, ScoreboardAction.CHANGE);
      sendTeamPacket(index, TeamMode.CREATE);

      oldLines.add(oldLines.size() - index, this.getLineByScore(index));
    }
  }

  public int size() {
    return lines.size();
  }

  public void delete() {
    int count = 0;
    while(true) {
      if (count >= size()) {
        this.sendObjectivePacket(REMOVE);
        break;
      }

      sendTeamPacket(count, TeamMode.REMOVE);
      count++;
    }

    this.deleted = true;
  }

  protected boolean hasLinesMaxLength() {
    return !V1_13.isHigherOrEqual();
  }

  private int getScoreByLine(final int line) {
    return size() - line - 1;
  }

  private String getLineByScore(final int score) {
    return getLineByScore(lines, score);
  }

  private String getLineByScore(final List<String> lines, final int score) {
    return lines.get(lines.size() - score - 1);
  }

  private void checkLineNumber(final int line, final boolean checkInRange, final boolean checkMax) {
    if (line < 0) {
      throw new IllegalArgumentException("Line number must be positive");
    } 
    
    if (checkInRange && line >= size()) {
      throw new IllegalArgumentException("Line number must be under " + size());
    } 
    
    if (checkMax && line >= COLOR_CODES.length - 1) {
      throw new IllegalArgumentException("Line number is too high: " + size());
    }
  }
  
  @SneakyThrows
  private void sendObjectivePacket(final ObjectiveMode mode) {
    final Object packet = PACKET_SB_OBJ.newInstance();

    setField(packet, String.class, id);
    setField(packet, Integer.TYPE, mode.ordinal());

    if (mode == REMOVE) {
      sendPacket(packet);
    }

    setComponentField(packet, title);
    if (V1_8.isHigherOrEqual()) {
      setField(packet, ENUM_SB_HEALTH_DISPLAY, ENUM_SB_HEALTH_DISPLAY_INTEGER);
    }
  }

  @SneakyThrows
  private void sendDisplayObjectivePacket(){
    final Object packet = PACKET_SB_DISPLAY_OBJ.newInstance();

    setField(packet, Integer.TYPE, 1);
    setField(packet, String.class, this.id);

    sendPacket(packet);
  }

  @SneakyThrows
  private void sendScorePacket(final int score, final ScoreboardAction action) {
    final Object packet = PACKET_SB_SCORE.newInstance();
    setField(packet, String.class, COLOR_CODES[score]);

    if (V1_8.isHigherOrEqual()) {
      setField(packet, ENUM_SB_ACTION, action == ScoreboardAction.REMOVE ? ENUM_SB_ACTION_REMOVE : ENUM_SB_ACTION_CHANGE);
    }

    if (action == ScoreboardAction.CHANGE) {
      setField(packet, String.class, id);
      setField(packet, Integer.TYPE, score);
    }

    sendPacket(packet);
  }

  @SneakyThrows
  private void sendTeamPacket(final int score, final TeamMode mode) {
    if (mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
      throw new UnsupportedOperationException();
    }

    final int maxLength = hasLinesMaxLength() ? 16 : 1024;
    final Object packet = PACKET_SB_TEAM.newInstance();

    setField(packet, String.class, id + ':' + score);
    setField(packet, Integer.TYPE, mode.ordinal());

    if (mode != TeamMode.CREATE && mode != TeamMode.UPDATE) {
      sendPacket(packet);
    }

    String line = getLineByScore(score);
    String suffix = null;
    String prefix;

    if (line == null || line.isEmpty()) {
      prefix = COLOR_CODES[score] + ChatColor.RESET;
    } else {
      if (line.length() <= maxLength) {
        prefix = line;
      } else {
        final int index = line.charAt(maxLength - 1) == 167 ? maxLength - 1 : maxLength;
        prefix = line.substring(0, index);

        final String suffixTmp = line.substring(index);

        ChatColor chatColor = null;
        if (suffixTmp.length() >= 2 && suffixTmp.charAt(0) == 167) {
          chatColor = ChatColor.getByChar(suffixTmp.charAt(1));
        }

        final String color = ChatColor.getLastColors(prefix);
        suffix = (chatColor == null || chatColor.isFormat() ? (color.isEmpty() ? ChatColor.RESET.toString() : color) : "") + suffixTmp;
      }
    }

    if (prefix.length() > maxLength || suffix != null && suffix.length() > maxLength) {
      prefix = prefix.substring(0, maxLength);
      suffix = suffix != null ? suffix.substring(0, maxLength) : null;
    }

    if (mode == TeamMode.CREATE) {
      setField(packet, Collection.class, Collections.singletonList(COLOR_CODES[score]));
    }

//    if (V1_13.isHigherOrEqual()) {
//      final Object team = PACKET_SB_SERIALIZABLE_TEAM.newInstance();
//
//      setComponentField(team, "");
//      setField(team, CHAT_FORMAT_ENUM, RESET_FORMATTING);
//      setComponentField(team, prefix);
//      setComponentField(team, suffix == null ? "" : suffix);
//      setField(team, String.class, "always");
//      setField(team, String.class, "always");
//      setField(packet, Optional.class, Optional.of(team));
//
//      sendPacket(packet);
//      return;
//    }

    setComponentField(packet, prefix);
    setComponentField(packet, suffix == null ? "" : suffix);
    setField(packet, String.class, "always");
    setField(packet, String.class, "always");

    sendPacket(packet);
  }

  @SneakyThrows
  private void sendPacket(final Object packet) {
    if (deleted) {
      return;
    }

    if (!player.isOnline()) {
      return;
    }

    final Object entityPlayer = PLAYER_GET_HANDLE.invoke(player);
    SEND_PACKET.invoke(PLAYER_CONNECTION.invoke(entityPlayer), packet);
  }

  @SneakyThrows
  private void setComponentField(Object packet, String value) {
    if (V1_13.isHigherOrEqual()) {
      setField(packet, String.class, value);
      return;
    }

    for (Field field : PACKETS.get(packet.getClass())) {
      if (field.getType() == String.class || field.getType() == CHAT_COMPONENT_CLASS) {
        field.set(packet, value.isEmpty()
            ? EMPTY_MESSAGE
            : Array.get(MESSAGE_FROM_STRING.invoke(value), 0));
      }
    }
  }

  enum VersionType {
    V1_8,
    V1_13;

    public boolean isHigherOrEqual() {
      return VERSION_TYPE.ordinal() >= this.ordinal();
    }
  }

  enum ScoreboardAction {

    CHANGE,
    REMOVE

  }

  enum TeamMode {

    CREATE,
    REMOVE,
    UPDATE,
    ADD_PLAYERS,
    REMOVE_PLAYERS

  }

  enum ObjectiveMode {

    CREATE,
    REMOVE,
    UPDATE

  }

}
