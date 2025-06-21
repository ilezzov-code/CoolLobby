package ru.ilezzov.coollobby.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacySerialize {
    private static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("§[0-9a-fk-orA-FK-OR]");
    private static final Pattern LEGACY_HEX_PATTERN = Pattern.compile("§#([0-9A-Fa-f]{6})");
    private static final Pattern LEGACY_HEX_PARAGRAPH_PATTERN = Pattern.compile("§x(§([0-9A-Fa-f])){6}");
    private static final Pattern LEGACY_ADVANCED_COLOR = Pattern.compile("##([a-fA-F\\d]{6})");
    private static final Map<String, String> COLORS = Map.ofEntries(
            Map.entry("§0", "#000000"), Map.entry("§1", "#0000AA"), Map.entry("§2", "#00AA00"),
            Map.entry("§3", "#00AAAA"), Map.entry("§4", "#AA0000"), Map.entry("§5", "#AA00AA"),
            Map.entry("§6", "#FFAA00"), Map.entry("§7", "#AAAAAA"), Map.entry("§8", "#555555"),
            Map.entry("§9", "#5555FF"), Map.entry("§a", "#55FF55"), Map.entry("§b", "#55FFFF"),
            Map.entry("§c", "#FF5555"), Map.entry("§d", "#FF55FF"), Map.entry("§e", "#FFFF55"),
            Map.entry("§f", "#FFFFFF"), Map.entry("§k", "obf"), Map.entry("§l", "b"),
            Map.entry("§m", "st"), Map.entry("§n", "u"), Map.entry("§o", "i"), Map.entry("§r", "reset")
    );

    public static Component serialize(final String message) {
        if (message == null) {
            return Component.empty();
        }
        return MiniMessage.miniMessage().deserialize(legacySerialize(message));
    }

    public static String serializeToString(final String message) {
        if (message == null) {
            return "";
        }
        return legacySerialize(message);
    }

    private static String legacySerialize(final String message) {
        String serializeMessage = translateAlternateCodeColor('&', message);

        serializeMessage = replaceLegacyParagraphHex(serializeMessage);
        serializeMessage = replaceLegacyAdvancedColor(serializeMessage);
        serializeMessage = replaceLegacyHex(serializeMessage);
        serializeMessage = replaceLegacyColor(serializeMessage);

        return serializeMessage;
    }

    private static String replaceLegacyAdvancedColor(final String s) {
        final Matcher matcher = LEGACY_ADVANCED_COLOR.matcher(s);
        final StringBuilder result = new StringBuilder(s.length());

        int lastIndex = 0;
        while (matcher.find()) {
            result.append(s, lastIndex, matcher.start())
                    .append("#")
                    .append(matcher.group(1));
            lastIndex = matcher.end();
        }

        result.append(s, lastIndex, s.length());
        return result.toString();
    }

    private static String replaceLegacyParagraphHex(final String s) {
        final Matcher matcher = LEGACY_HEX_PARAGRAPH_PATTERN.matcher(s);
        final StringBuilder result = new StringBuilder();

        int lastIndex = 0;
        while (matcher.find()) {
            final String hexColor = matcher.group().replace("§x", "").replace("§", "");

            result.append(s, lastIndex, matcher.start())
                    .append("<#").append(hexColor).append(">");

            lastIndex = matcher.end();
        }

        result.append(s, lastIndex, s.length());
        return result.toString();
    }

    private static String replaceLegacyHex(final String s) {
        final Matcher matcher = LEGACY_HEX_PATTERN.matcher(s);
        final StringBuilder result = new StringBuilder();

        int lastIndex = 0;
        while (matcher.find()) {
            result.append(s, lastIndex, matcher.start())
                    .append("<#").append(matcher.group(1)).append(">");
            lastIndex = matcher.end();
        }

        result.append(s, lastIndex, s.length());
        return result.toString();
    }

    private static String replaceLegacyColor(final String s) {
        final Matcher matcher = LEGACY_COLOR_PATTERN.matcher(s);
        final StringBuilder result = new StringBuilder();

        int lastIndex = 0;
        while (matcher.find()) {
            final String replacement = COLORS.getOrDefault(matcher.group(), matcher.group());
            result.append(s, lastIndex, matcher.start()).append("<").append(replacement).append(">");
            lastIndex = matcher.end();
        }

        result.append(s, lastIndex, s.length());
        return result.toString();
    }

    private static String translateAlternateCodeColor(char code, String s) {
        char[] b = s.toCharArray();

        for(int i = 0; i < b.length - 1; ++i) {
            if (b[i] == code && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx#".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }
}
