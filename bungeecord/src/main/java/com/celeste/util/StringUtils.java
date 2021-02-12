package com.celeste.util;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class StringUtils {

    public TextComponent format(List<String> msg) {
        final TextComponent component = new TextComponent();
        for (String arg : msg) {
            component.addExtra(arg.replace("&", "§"));
        }

        return component;
    }

    public TextComponent format(String[] msg) {
        final TextComponent component = new TextComponent();
        for (String arg : msg) {
            component.addExtra(arg.replace("&", "§"));
        }

        return component;
    }

    public TextComponent format(String msg) {
        final TextComponent component = new TextComponent();
        component.addExtra(msg.replace("&", "§"));

        return component;
    }

    public String formatColor(String message) {
        message = message.replace("&", "§");

        return message;
    }

}
