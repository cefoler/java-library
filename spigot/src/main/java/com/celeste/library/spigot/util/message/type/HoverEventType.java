package com.celeste.library.spigot.util.message.type;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HoverEventType {

  SHOW_TEXT("show_text"),
  SHOW_ITEM("show_item");

  private final String name;

  public static HoverEventType getEvent(final String event) {
    return Arrays.stream(values())
        .filter(type -> type.getName().equalsIgnoreCase(event))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid event: " + event));
  }

}