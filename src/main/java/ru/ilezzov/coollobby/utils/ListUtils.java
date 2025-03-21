package ru.ilezzov.pluginBlank.utils;

import java.util.List;

public class ListUtils {
    public static String listToString(List<String> list) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));

            if (list.size() - i != 1) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }
}
