package ru.ilezzov.pluginBlank.messages;

import net.kyori.adventure.text.Component;
import ru.ilezzov.pluginBlank.utils.Placeholder;

import java.util.HashMap;

import static ru.ilezzov.pluginBlank.Main.*;

public class PluginMessages {
    public static Component pluginEnableMessage(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.enable", placeholders);
    }

    public static Component pluginDisableMessage(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.disable", placeholders);
    }

    public static Component pluginReloadMessage(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.reload", placeholders);
    }

    public static Component pluginNoPermsMessage(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.no_perms", placeholders);
    }

    public static Component pluginNoConsoleMessage(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.no_console", placeholders);
    }

    public static Component pluginHasErrorMessageEnable(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.has_error_enable", placeholders);
    }

    public static Component pluginHasErrorMessageReload(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.has_error_reload", placeholders);
    }

    public static Component pluginLatestVersionMessage(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.latest_version", placeholders);
    }

    public static Component pluginOutdatedVersionMessage(final HashMap<String, String> placeholders) {
        return getComponent("plugin_messages.outdated_version", placeholders);
    }

    public static Component eventPlayerJoinMessage(final HashMap<String, String> placeholders) {
        return getComponent("event_messages.player_join", placeholders);
    }

    public static Component commandMainCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("command_messages.main_command", placeholders);
    }

    private static Component getComponent(final String key) {
        final String message = getMessages().getString(key);

        return getLegacySerialize().serialize(message);
    }

    private static Component getComponent(final String key, final HashMap<String, String> placeholders) {
        String message = getMessages().getString(key);
        message = replacePlaceholder(message, placeholders);

        return getLegacySerialize().serialize(message);
    }

    private static String replacePlaceholder(final String message, final HashMap<String, String> placeholders) {
        return Placeholder.replacePlaceholder(message, placeholders);
    }

}
