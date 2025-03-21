package ru.ilezzov.pluginBlank.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacySerialize {
    private final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("§[0-9a-fk-orA-FK-OR]");
    private final Pattern LEGACY_HEX_PATTERN = Pattern.compile("§#([a-fA-F\\d]{6})");
    private final Pattern LEGACY_HEX_PARAGRAPH_PATTERN = Pattern.compile("§x(§[0-9A-Fa-f]){6}");
    private final Pattern LEGACY_ADVANCED_COLOR = Pattern.compile("##([a-fA-F\\d]{6})");
    private final HashMap<String, String> colors;

    public LegacySerialize() {
        this.colors = new HashMap<>();

        colors.put("§0", "#000000");
        colors.put("§1", "#0000AA");
        colors.put("§2", "#00AA00");
        colors.put("§3", "#00AAAA");
        colors.put("§4", "#AA0000");
        colors.put("§5", "#AA00AA");
        colors.put("§6", "#FFAA00");
        colors.put("§7", "#AAAAAA");
        colors.put("§8", "#555555");
        colors.put("§9", "#5555FF");
        colors.put("§a", "#55FF55");
        colors.put("§b", "#55FFFF");
        colors.put("§c", "#FF5555");
        colors.put("§d", "#FF55FF");
        colors.put("§e", "#FFFF55");
        colors.put("§f", "#FFFFFF");
        colors.put("§k", "obf");
        colors.put("§l", "b");
        colors.put("§m", "st");
        colors.put("§n", "u");
        colors.put("§o", "i");
        colors.put("§r", "reset");
    }

    public Component serialize(final String message) {
        return MiniMessage.miniMessage().deserialize(legacySerialize(message));
    }

    public String serializeToString(final String message) {
        return legacySerialize(message);
    }

    private String legacySerialize(final String message) {
        String serializeMessage = translateAlternateCodeColor('&', message);

        serializeMessage = replaceLegacyParagraphHex(serializeMessage);
        serializeMessage = replaceLegacyAdvancedColor(serializeMessage);
        serializeMessage = replaceLegacyHex(serializeMessage);
        serializeMessage = replaceLegacyColor(serializeMessage);

        return serializeMessage;
    }

    private String replaceLegacyAdvancedColor(final String s) {
        final Matcher matcher = LEGACY_ADVANCED_COLOR.matcher(s);
        final StringBuilder result = new StringBuilder(s.length());
        int lastIndex = 0;

        while (matcher.find()) {
            final String matcherGroup = matcher.group();

            result.append(s, lastIndex, matcher.start())
                    .append(matcherGroup.charAt(1))
                    .append(matcherGroup.charAt(2))
                    .append(matcherGroup.charAt(3))
                    .append(matcherGroup.charAt(4))
                    .append(matcherGroup.charAt(5))
                    .append(matcherGroup.charAt(6))
                    .append(matcherGroup.charAt(7));
            lastIndex = matcher.end();
        }

        result.append(s, lastIndex, s.length());
        return result.toString();
    }

    private String replaceLegacyParagraphHex(final String s) {
        final Matcher matcher = LEGACY_HEX_PARAGRAPH_PATTERN.matcher(s);
        final StringBuilder result = new StringBuilder(s.length());
        int lastIndex = 0;

        while (matcher.find()) {
            final String matcherGroup = matcher.group();

            result.append(s, lastIndex, matcher.start()).append("<#")
                    .append(matcherGroup.charAt(3))
                    .append(matcherGroup.charAt(5))
                    .append(matcherGroup.charAt(7))
                    .append(matcherGroup.charAt(9))
                    .append(matcherGroup.charAt(11))
                    .append(matcherGroup.charAt(13))
                    .append(">");
            lastIndex = matcher.end();
        }

        result.append(s, lastIndex, s.length());
        return result.toString();
    }

    private String replaceLegacyHex(final String s) {
        final Matcher matcher = LEGACY_HEX_PATTERN.matcher(s);
        final StringBuilder result = new StringBuilder(s.length());
        int lastIndex = 0;

        while (matcher.find()) {
            final String matcherGroup = matcher.group();
            result.append(s, lastIndex, matcher.start()).append("<#")
                    .append(matcherGroup.charAt(2))
                    .append(matcherGroup.charAt(3))
                    .append(matcherGroup.charAt(4))
                    .append(matcherGroup.charAt(5))
                    .append(matcherGroup.charAt(6))
                    .append(matcherGroup.charAt(7))
                    .append(">");
            lastIndex = matcher.end();
        }

        result.append(s, lastIndex, s.length());
        return result.toString();
    }

    private String replaceLegacyColor(final String s) {
        final Matcher matcher = LEGACY_COLOR_PATTERN.matcher(s);
        final StringBuilder result = new StringBuilder(s.length());
        int lastIndex = 0;

        while (matcher.find()) {
            result.append(s, lastIndex, matcher.start()).append("<").append(colors.get(matcher.group())).append(">");
            lastIndex = matcher.end();
        }

        result.append(s, lastIndex, s.length());
        return result.toString();
    }

    private String translateAlternateCodeColor(char code, String s) {
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
