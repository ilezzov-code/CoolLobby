package ru.ilezzov.coollobby.utils;

import java.util.List;

public class ListUtils {
    public static String listToString(final List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        return String.join(", ", list);
    }
}
