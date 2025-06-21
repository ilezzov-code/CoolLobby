package ru.ilezzov.coollobby.commands.executors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.api.CoolLobbyApi;
import ru.ilezzov.coollobby.database.data.spawn.SpawnData;
import ru.ilezzov.coollobby.database.data.spawn.SpawnDataRepository;
import ru.ilezzov.coollobby.managers.CooldownManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.permission.Permission;
import ru.ilezzov.coollobby.permission.PermissionsChecker;
import ru.ilezzov.coollobby.placeholder.PluginPlaceholder;

import java.util.*;

public class SpawnCommand implements CommandExecutor, TabCompleter {
    private final boolean isEnable = Main.getConfigFile().getBoolean("spawn_command.enable");

    private final CooldownManager cooldownManager = new CooldownManager(Main.getConfigFile().getInt("spawn_command.cooldown"));
    private final PluginPlaceholder commandPlaceholders = new PluginPlaceholder();
    private final CoolLobbyApi api = Main.getApi();

    private final SpawnDataRepository spawnDataRepository = Main.getSpawnDataRepository();
    private final Map<UUID, SpawnData> overwriteSpawns = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String[] args) {
        if (!isEnable) {
            sender.sendMessage(PluginMessages.commandDisableCommandMessage(commandPlaceholders));
            return true;
        }

        if (args.length == 0) {
            return handleRandomSpawn(sender);
        }

        if (sender instanceof Player player) {
            if (cooldownManager.checkCooldown(player.getUniqueId())) {
                commandPlaceholders.addPlaceholder("{TIME}", cooldownManager.getRemainingTime(player.getUniqueId()));
                sender.sendMessage(PluginMessages.pluginCommandCooldownMessage(commandPlaceholders));
                return true;
            }
        }

        final String sub = args[0].toLowerCase();

        return switch (sub) {
            case "set" -> handleSetSpawn(sender);
            case "remove" -> handleRemoveSpawn(sender, args);
            case "confirm" -> handleConfirmSpawn(sender);
            case "help" -> {
                sender.sendMessage(PluginMessages.commandSpawnHelpMessage(commandPlaceholders));
                yield true;
            }
            default -> {
                if (args.length == 1) {
                    yield handleSingleArgument(sender, args[0]);
                } else if (args.length == 2) {
                    yield handleDoubleArgument(sender, args[0], args[1]);
                } else {
                    sender.sendMessage(PluginMessages.commandSpawnHelpMessage(commandPlaceholders));
                    yield true;
                }
            }
        };
    }

    private boolean handleRandomSpawn(final CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders));
            return true;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.SPAWN_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
            return true;
        }

        api.teleportToSpawn(player).thenAccept(response -> {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                switch (response.status()) {
                    case 0 -> sender.sendMessage(PluginMessages.commandSpawnNotSetMessage(commandPlaceholders));
                    case 1 -> {
                        commandPlaceholders.addPlaceholder("{SPAWN_NAME}", response.data());
                        sender.sendMessage(PluginMessages.commandSpawnTeleportMessage(commandPlaceholders));
                        newCooldown(sender);
                    }
                }
            });
        });

        return true;
    }

    private boolean handleSetSpawn(final CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders));
            return true;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.SPAWN_SET_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
            player.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
            return true;
        }

        api.setSpawn(player).thenAccept(response -> {
           Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
               switch (response.status()) {
                   case 1 -> {
                       commandPlaceholders.addPlaceholder("{SPAWN_NAME}", response.data());
                       player.sendMessage(PluginMessages.commandSpawnSetMessage(commandPlaceholders));
                   }
                   case 0 -> {
                       final SpawnData spawnData = (SpawnData) response.data();

                       overwriteSpawns.put(player.getUniqueId(), spawnData);
                       commandPlaceholders.addPlaceholder("{SPAWN_NANE}", spawnData.getName());
                       player.sendMessage(PluginMessages.commandSpawnSetOverwriteMessage(commandPlaceholders));
                   }
               }
           });
        });

        return true;
    }

    private boolean handleRemoveSpawn(final CommandSender sender, final String[] args) {
        if (!PermissionsChecker.hasPermission(sender, Permission.SPAWN_REMOVE_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
            return true;
        }

        String targetName;

        if (args.length == 1) {
            if (sender instanceof Player player) {
                targetName = player.getWorld().getName();
            } else {
                sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders));
                return true;
            }
        } else {
            targetName = args[1];
        }

        api.removeSpawn(targetName).thenAccept(response -> {
           Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
               switch (response.status()) {
                   case -1 -> {
                       commandPlaceholders.addPlaceholder("{ERROR}", "An error occurred when executing a database query. Contact the server administrator");
                       sender.sendMessage(PluginMessages.pluginHasErrorMessage(commandPlaceholders));
                   }
                   case 0 -> {
                       commandPlaceholders.addPlaceholder("{SPAWN_NAME}", targetName);
                       sender.sendMessage(PluginMessages.commandSpawnNotFoundMessage(commandPlaceholders));
                   }
                   case 1 -> {
                       commandPlaceholders.addPlaceholder("{SPAWN_NAME}", targetName);
                       sender.sendMessage(PluginMessages.commandSpawnRemoveMessage(commandPlaceholders));
                   }
               }
           });
        });

        return true;
    }

    private boolean handleConfirmSpawn(final CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders));
            return true;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.SPAWN_SET_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
            return true;
        }

        final SpawnData spawnData = overwriteSpawns.get(player.getUniqueId());

        if (spawnData == null) {
            sender.sendMessage(PluginMessages.commandSpawnNotFoundConfirmMessage(commandPlaceholders));
            return true;
        }

        api.updateSpawn(spawnData.getName(), player.getLocation());
        commandPlaceholders.addPlaceholder("{SPAWN_NAME}", spawnData.getName());
        sender.sendMessage(PluginMessages.commandSpawnSetMessage(commandPlaceholders));

        return true;
    }


    private boolean handleSingleArgument(CommandSender sender, String arg) {
        // Try as player name
        final Player target = Bukkit.getPlayerExact(arg);
        if (target != null) {
            if (!PermissionsChecker.hasPermission(sender, Permission.SPAWN_OTHER_PLAYER_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
                sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
                return true;
            }

            api.teleportToSpawn(target).thenAccept(response -> {
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    switch (response.status()) {
                        case 0 -> sender.sendMessage(PluginMessages.commandSpawnNotSetMessage(commandPlaceholders));
                        case 1 -> {
                            commandPlaceholders.addPlaceholder("{SPAWN_NAME}", response.data());
                            commandPlaceholders.addPlaceholder("{PLAYER}", sender.getName());
                            target.sendMessage(PluginMessages.commandSpawnTeleportByOtherMessage(commandPlaceholders));

                            commandPlaceholders.addPlaceholder("{PLAYER}", target.getName());
                            sender.sendMessage(PluginMessages.commandSpawnTeleportOtherMessage(commandPlaceholders));

                            newCooldown(sender);
                        }
                    }
                });
            });

            return true;
        }

        if (!(sender instanceof Player player)) {
            commandPlaceholders.addPlaceholder("{PLAYER}", arg);
            sender.sendMessage(PluginMessages.pluginPlayerNotFoundMessage(commandPlaceholders));
            return true;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.SPAWN_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
            return true;
        }

        api.teleportToSpawn(player, arg).thenAccept(response -> {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                switch (response.status()) {
                    case 0 -> {
                        commandPlaceholders.addPlaceholder("{SPAWN_NAME}", arg);
                        sender.sendMessage(PluginMessages.commandSpawnNotFoundMessage(commandPlaceholders));
                    }
                    case 1 -> {
                        commandPlaceholders.addPlaceholder("{SPAWN_NAME}", response.data());
                        sender.sendMessage(PluginMessages.commandSpawnTeleportMessage(commandPlaceholders));
                        newCooldown(sender);
                    }
                }
            });
        });

        return true;
    }

    private boolean handleDoubleArgument(CommandSender sender, String spawnName, String targetName) {
        if (!PermissionsChecker.hasPermission(sender, Permission.SPAWN_OTHER_PLAYER_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
            return true;
        }

        final Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            commandPlaceholders.addPlaceholder("{PLAYER}", targetName);
            sender.sendMessage(PluginMessages.pluginPlayerNotFoundMessage(commandPlaceholders));
            return true;
        }

        api.teleportToSpawn(target, spawnName).thenAccept(response -> {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                switch (response.status()) {
                    case 0 -> {
                        commandPlaceholders.addPlaceholder("{SPAWN_NAME}", spawnName);
                        sender.sendMessage(PluginMessages.commandSpawnNotFoundMessage(commandPlaceholders));
                    }
                    case 1 -> {
                        commandPlaceholders.addPlaceholder("{SPAWN_NAME}", response.data());
                        commandPlaceholders.addPlaceholder("{PLAYER}", sender.getName());
                        target.sendMessage(PluginMessages.commandSpawnTeleportByOtherMessage(commandPlaceholders));

                        commandPlaceholders.addPlaceholder("{PLAYER}", target.getName());
                        sender.sendMessage(PluginMessages.commandSpawnTeleportOtherMessage(commandPlaceholders));

                        newCooldown(sender);
                    }
                }
            });
        });

        return true;
    }

    private void newCooldown(final CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if (PermissionsChecker.hasPermission(player, Permission.NO_COOLDOWN)) {
            return;
        }

        cooldownManager.newCooldown(player.getUniqueId());
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        final Set<String> spawnNames = spawnDataRepository.asMap().keySet();

        if (args.length == 1) {
            final String partialName = args[0].toLowerCase();

            if (PermissionsChecker.hasPermission(sender, Permission.SPAWN_SET_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
                if ("set".startsWith(partialName)) {
                    completions.add("set");
                }
                if ("confirm".startsWith(partialName)) {
                    completions.add("confirm");
                }
            }
            if (PermissionsChecker.hasPermission(sender, Permission.SPAWN_REMOVE_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
                if ("remove".startsWith(partialName)) {
                    completions.add("remove");
                }
            }
            if (PermissionsChecker.hasPermission(sender, Permission.SPAWN_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
                spawnNames.stream()
                        .filter(name -> name.toLowerCase().startsWith(partialName))
                        .forEach(completions::add);

                Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(partialName))
                        .forEach(completions::add);
            }
        }

        if (args.length == 2) {
            final String arg0 = args[0];

            if (arg0.equalsIgnoreCase("remove")) {
                if (PermissionsChecker.hasPermission(sender, Permission.SPAWN_REMOVE_COMMAND, Permission.SPAWN_ALL_COMMAND)) {
                    final String spawnName = args[1].toLowerCase();
                    spawnNames.stream()
                            .filter(name -> name.toLowerCase().startsWith(spawnName))
                            .forEach(completions::add);
                }
            } else {
                final String partialName = args[1];
                Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(partialName))
                        .forEach(completions::add);
            }
        }

        return completions;
    }
}
