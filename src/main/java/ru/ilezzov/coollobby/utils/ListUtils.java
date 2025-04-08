package ru.ilezzov.coollobby.utils;

import org.bukkit.World;

import java.util.Collection;
import java.util.stream.Collectors;

public class ListUtils {
    public static String listToString(final Collection<String> collection) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        return String.join(", ", collection);
    }

    public static String worldsToString(final Collection<World> collection) {
        return collection.stream()
                .map(World::getName)
                .collect(Collectors.joining(", "));
    }
}
