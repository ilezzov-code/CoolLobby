package ru.ilezzov.coollobby.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.commands.executors.*;
import ru.ilezzov.coollobby.logging.Logger;

import java.util.Map;

import static ru.ilezzov.coollobby.messages.ConsoleMessages.addNewKeysToFile;
import static ru.ilezzov.coollobby.messages.ConsoleMessages.errorOccurred;

public class CommandManager {
    private static final Logger logger = Main.getPluginLogger();
    private static final JavaPlugin plugin = Main.getInstance();

    public static void loadCommands() {
        final Map<String, CommandExecutor> commands = getCommands();

        for (final String commandName : commands.keySet()) {
            final PluginCommand command = plugin.getCommand(commandName);

            if (command != null) {
                final CommandExecutor commandExecutor = commands.get(commandName);
                command.setExecutor(commandExecutor);

                if (commandExecutor instanceof TabCompleter completer) {
                    command.setTabCompleter(completer);
                }
            } else {
                logger.info(errorOccurred(String.format("The command %s was not found in plugin.yml", commandName)));
            }
        }
    }

    private static Map<String, CommandExecutor> getCommands() {
        return Map.ofEntries(
                Map.entry("coollobby", new MainCommand()),
                Map.entry("firework", new FireworkCommand()),
                Map.entry("lighting", new LightingCommand()),
                Map.entry("fly", new FlyCommand()),
                Map.entry("spit", new SpitCommand()),
                Map.entry("spawn", new SpawnCommand())
        );
    }
}
