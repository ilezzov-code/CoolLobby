package ru.ilezzov.coollobby.messages;

import net.kyori.adventure.text.Component;
import ru.ilezzov.coollobby.utils.PlaceholderReplacer;

import java.util.HashMap;

import static ru.ilezzov.coollobby.Main.*;

public class PluginMessages {

    public static Component pluginReloadMessage(final HashMap<String, String> placeholders) {
        return getComponent("Plugin.plugin-reload", placeholders);
    }

    public static Component pluginHasErrorMessage(final HashMap<String, String> placeholders) {
        return getComponent("Plugin.plugin-has-error", placeholders);
    }

    public static Component pluginHasErrorMessageEnable(final HashMap<String, String> placeholders) {
        return getComponent("Plugin.plugin-has-error-enable", placeholders);
    }

    public static Component pluginHasErrorMessageReload(final HashMap<String, String> placeholders) {
        return getComponent("Plugin.plugin-has-error-reload", placeholders);
    }

    public static Component pluginLatestVersionMessage(final HashMap<String, String> placeholders) {
        return getComponent("Plugin.plugin-use-latest-version", placeholders);
    }

    public static Component pluginOutdatedVersionMessage(final HashMap<String, String> placeholders) {
        return getComponent("Plugin.plugin-use-outdated-version", placeholders);
    }

    public static Component pluginNoPermsMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-permission-error", placeholders);
    }

    public static Component pluginNoConsoleMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-no-console-error", placeholders);
    }

    public static Component pluginCooldownMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-cooldown", placeholders);
    }

    public static Component commandMainCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-main-message", placeholders);
    }

    public static Component commandFwCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-firework-message", placeholders);
    }

    public static Component commandLtCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-lighting-message", placeholders);
    }

    public static Component commandSpitCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-spit-message", placeholders);
    }

    public static Component commandFlyEnableCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-fly-enable-message", placeholders);
    }

    public static Component commandFlyDisableCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-fly-disable-message", placeholders);
    }

    public static Component commandDisableCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-disable", placeholders);
    }

    public static Component commandOnlyLobbyCommandMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.command-only-lobby-use", placeholders);
    }

    public static Component eventPlayerJoinMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.player-join-message", placeholders);
    }

    public static Component eventPlayerJoinGlobalMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.player-join-global-message", placeholders);
    }

    public static Component eventPlayerLeaveGlobalMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.player-leave-global-message", placeholders);
    }

    public static Component doubleJumpJumpMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.double-jump-jump-message", placeholders);
    }

    public static Component doubleJumpCooldownMessage(final HashMap<String, String> placeholders) {
        return getComponent("Messages.double-jump-cooldown-message", placeholders);
    }

    public static Component eventPlayerJoinTitleTitle() {
        return getComponent("Title.player-join-title");
    }

    public static Component eventPlayerJoinTitleSubtitle() {
        return getComponent("Title.player-join-subtitle");
    }

    private static Component getComponent(final String key) {
        final String message = getMessagesFile().getConfig().getString(key);

        return getLegacySerialize().serialize(message);
    }

    private static Component getComponent(final String key, final HashMap<String, String> placeholders) {
        String message = getMessagesFile().getString(key);
        message = replacePlaceholder(message, placeholders);

        return getLegacySerialize().serialize(message);
    }

    private static String replacePlaceholder(final String message, final HashMap<String, String> placeholders) {
        return PlaceholderReplacer.replacePlaceholder(message, placeholders);
    }

}
