package ru.ilezzov.coollobby.messages;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.settings.PluginSettings;
import ru.ilezzov.coollobby.utils.LegacySerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConsoleMessages {
    private static final PluginSettings settings = Main.getPluginSettings();
    private static final ConfigurationSection messages = settings.getSection("Messages");

    public static Component createNewFile(final String fileName) {
        return getComponent(Objects.requireNonNull(messages.getString("create-new-file")), fileName);
    }

    public static Component addNewKeysToFile(final String fileName) {
        return getComponent(Objects.requireNonNull(messages.getString("add-new-keys-to-file")), fileName);
    }

    public static List<Component> legacyPluginVersion(final String outdatedVersion, final String latestVersion, final String downloadLink) {
        return getComponents(Objects.requireNonNull(messages.getString("legacy-plugin-version")), outdatedVersion, latestVersion, downloadLink);
    }

    public static Component latestPluginVersion(final String pluginVersion) {
        return getComponent(Objects.requireNonNull(messages.getString("latest-plugin-version")), pluginVersion);
    }

    public static Component errorOccurred(final String errorMessage) {
        return getComponent(Objects.requireNonNull(messages.getString("error-occurred")), errorMessage);
    }

    public static Component successConnectToDatabase() {
        return getComponent(Objects.requireNonNull(messages.getString("success-connect-to-database")));
    }

    public static Component databaseError(final String errorMessage) {
        return getComponent(Objects.requireNonNull(messages.getString("database-error")), errorMessage);
    }

    public static Component savePlayer(final String displayName) {
        return getComponent(Objects.requireNonNull(messages.getString("save-player")), displayName);
    }

    public static Component saveQueue() {
        return getComponent(Objects.requireNonNull(messages.getString("save-queue")));
    }

    public static List<Component> pluginEnable(final String pluginDeveloper, final String pluginVersion, final String pluginContactLink) {
        return getComponents(Objects.requireNonNull(messages.getString("plugin-enabled")), pluginDeveloper, pluginVersion, pluginContactLink);
    }

    public static List<Component> pluginDisable(final String pluginDeveloper, final String pluginVersion, final String pluginContactLink) {
        return getComponents(Objects.requireNonNull(messages.getString("plugin-disabled")), pluginDeveloper, pluginVersion, pluginContactLink);
    }

    public static Component eventLoaded(final String eventName) {
        return getComponent(Objects.requireNonNull(messages.getString("event-loaded")), eventName);
    }

    public static Component apiLoaded() {
        return getComponent(Objects.requireNonNull(messages.getString("api-loaded")));
    }

    public static Component worldLoaded(final String world) {
        return getComponent(Objects.requireNonNull(messages.getString("world-loaded")), world);
    }

    private static Component getComponent(final String message, final Object... keys) {
        return LegacySerialize.serialize(message.formatted(keys));
    }

    private static List<Component> getComponents(final String message, final Object... keys) {
        final String[] messageSplit = message.formatted(keys).split("\n");
        List<Component> components = new ArrayList<>();

        for (final String text : messageSplit) {
            components.add(LegacySerialize.serialize(text));
        }

        return components;
    }
}
