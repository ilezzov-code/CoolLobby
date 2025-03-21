package ru.ilezzov.coollobby.utils;

import java.util.HashMap;

public class Placeholder {
    public static String replacePlaceholder(String messages, final HashMap<String, String> placeholders) {
        for(final String placeholder : placeholders.keySet()) {
            messages = messages.replace(placeholder, placeholders.get(placeholder));
        }

        return messages;
    }

}
