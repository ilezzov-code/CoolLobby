package ru.ilezzov.coollobby.utils;

import ru.ilezzov.coollobby.models.PluginPlaceholder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderReplacer {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(.*?)}");

    public static String replacePlaceholder(final String message, final PluginPlaceholder placeholders) {
        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);
        final StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            final String placeholderKey = matcher.group();
            final String value = placeholders.getPlaceholders().getOrDefault(placeholderKey, placeholderKey).toString();
            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
