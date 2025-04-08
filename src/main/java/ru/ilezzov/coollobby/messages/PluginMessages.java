package ru.ilezzov.coollobby.messages;

import net.kyori.adventure.text.Component;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.models.PluginPlaceholder;
import ru.ilezzov.coollobby.utils.LegacySerialize;
import ru.ilezzov.coollobby.utils.PlaceholderReplacer;

public class PluginMessages {
    public static Component pluginOutdatedVersionMessage(final PluginPlaceholder placeholder) {
        return getComponent("Plugin.plugin-use-outdated-version", placeholder);
    }

    public static Component pluginReloadMessage(final PluginPlaceholder placeholder) {
        return getComponent("Plugin.plugin-reload", placeholder);
    }

    public static Component pluginNoPermsMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-permission-error", placeholder);
    }

    public static Component pluginNoConsoleMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-no-console-error", placeholder);
    }

    public static Component pluginCommandCooldownMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-cooldown", placeholder);
    }

    public static Component commandMainCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-main-message", placeholder);
    }

    public static Component commandDisableCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-disable", placeholder);
    }

    public static Component commandOnlyLobbyUse(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-only-lobby-use", placeholder);
    }

    public static Component commandFireworkCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-firework-message", placeholder);
    }

    public static Component commandLightingCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-lighting-message", placeholder);
    }

    public static Component commandSpitCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spit-message", placeholder);
    }

    public static Component commandFlyEnableCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-fly-enable-message", placeholder);
    }

    public static Component commandFlyDisableCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-fly-disable-message", placeholder);
    }

    public static Component playerJoinMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-join-message", placeholder);
    }

    public static Component playerJoinGlobalMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-join-global-message", placeholder);
    }
    
    public static Component playerLeaveGlobalMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-leave-global-message", placeholder);
    }

    public static Component playerDoubleJumpMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-double-jump-message", placeholder);
    }

    public static Component playerDoubleJumpActionBarCooldownMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-double-jump-action-bar-cooldown", placeholder);
    }

    public static Component playerJoinTitleTitle() {
        return getComponent("Title.player-join-title");
    }

    public static Component playerJoinTitleSubtitle() {
        return getComponent("Title.player-join-subtitle");
    }

    private static Component getComponent(final String key) {
        final String message = Main.getMessagesFile().getConfig().getString(key);

        return LegacySerialize.serialize(message);
    }

    private static Component getComponent(final String key, final PluginPlaceholder placeholder) {
        String message = Main.getMessagesFile().getString(key);
        message = replacePlaceholder(message, placeholder);

        return LegacySerialize.serialize(message);
    }

    private static String replacePlaceholder(final String message, final PluginPlaceholder placeholder) {
        return PlaceholderReplacer.replacePlaceholder(message, placeholder);
    }
}
